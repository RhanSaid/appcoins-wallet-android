package com.asfoundation.wallet

import com.appcoins.wallet.bdsbilling.WalletService
import com.asfoundation.wallet.util.convertToBase64
import com.google.gson.JsonObject

/**
 * Variable representing in seconds the time to live interval of the authentication token.
 **/
private const val TTL_IN_SECONDS = 3600

class EwtAuthenticatorService(private val walletService: WalletService,
                              private val header: String) {

  private var cachedAuth: MutableMap<String, Pair<String, Long>> = HashMap()

  @Synchronized
  fun getEwtAuthentication(address: String): String {
    return if (shouldBuildEwtAuth(address))
      getNewEwtAuthentication(address)
    else {
      cachedAuth[address]!!.first
    }
  }

  @Synchronized
  fun getNewEwtAuthentication(address: String): String {
    val currentUnixTime = System.currentTimeMillis() / 1000L
    val ewtString = buildEwtString(address, currentUnixTime)
    cachedAuth[address] = Pair(ewtString, currentUnixTime + TTL_IN_SECONDS)
    return ewtString
  }

  private fun shouldBuildEwtAuth(address: String): Boolean {
    val currentUnixTime = System.currentTimeMillis() / 1000L
    return !cachedAuth.containsKey(address) || hasExpired(currentUnixTime,
        cachedAuth[address]?.second)
  }

  private fun hasExpired(currentUnixTime: Long, ttlUnixTime: Long?): Boolean {
    return ttlUnixTime == null || currentUnixTime >= ttlUnixTime
  }

  private fun buildEwtString(address: String, currentUnixTime: Long): String {
    val header = replaceInvalidCharacters(header.convertToBase64())
    val payload = replaceInvalidCharacters(getPayload(address, currentUnixTime))
    val signedContent = walletService.signContent("$header.$payload")
        .blockingGet()
    return "Bearer $header.$payload.$signedContent"
  }

  private fun getPayload(walletAddress: String, currentUnixTime: Long): String {
    val payloadJson = JsonObject()
    cachedAuth[walletAddress] = Pair("", currentUnixTime)
    payloadJson.addProperty("iss", walletAddress)
    payloadJson.addProperty("exp", currentUnixTime + TTL_IN_SECONDS)
    return payloadJson.toString()
        .convertToBase64()
  }

  private fun replaceInvalidCharacters(ewtString: String): String {
    return ewtString.replace("=", "")
        .replace("+", "-")
        .replace("/", "_")
  }
}