package com.asfoundation.wallet.ui.iab

import android.util.Pair
import com.appcoins.wallet.bdsbilling.WalletService
import com.appcoins.wallet.gamification.repository.ForecastBonusAndLevel
import com.asfoundation.wallet.entity.Balance
import com.asfoundation.wallet.entity.TransactionBuilder
import com.asfoundation.wallet.support.SupportInteractor
import com.asfoundation.wallet.ui.balance.BalanceInteract
import com.asfoundation.wallet.ui.gamification.GamificationInteractor
import com.asfoundation.wallet.wallet_blocked.WalletBlockedInteract
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import java.math.BigDecimal
import java.util.*

class PaymentMethodsInteract(private val walletService: WalletService,
                             private val supportInteractor: SupportInteractor,
                             private val gamificationInteractor: GamificationInteractor,
                             private val balanceInteract: BalanceInteract,
                             private val walletBlockedInteract: WalletBlockedInteract,
                             private val inAppPurchaseInteractor: InAppPurchaseInteractor) {


  fun showSupport(gamificationLevel: Int): Completable {
    return walletService.getWalletAddress()
        .flatMapCompletable {
          Completable.fromAction {
            supportInteractor.registerUser(gamificationLevel, it.toLowerCase(Locale.ROOT))
            supportInteractor.displayChatScreen()
          }
        }
  }

  fun getEthBalance(): Observable<Pair<Balance, FiatValue>> = balanceInteract.getEthBalance()

  fun getAppcBalance(): Observable<Pair<Balance, FiatValue>> = balanceInteract.getAppcBalance()

  fun getCreditsBalance(): Observable<Pair<Balance, FiatValue>> =
      balanceInteract.getCreditsBalance()

  fun isBonusActiveAndValid() = gamificationInteractor.isBonusActiveAndValid()

  fun isBonusActiveAndValid(forecastBonus: ForecastBonusAndLevel) =
      gamificationInteractor.isBonusActiveAndValid(forecastBonus)

  fun getEarningBonus(packageName: String, amount: BigDecimal): Single<ForecastBonusAndLevel> =
      gamificationInteractor.getEarningBonus(packageName, amount)

  fun isWalletBlocked() = walletBlockedInteract.isWalletBlocked()

  fun getCurrentPaymentStep(packageName: String, transactionBuilder: TransactionBuilder)
      : Single<AsfInAppPurchaseInteractor.CurrentPaymentStep> =
      inAppPurchaseInteractor.getCurrentPaymentStep(packageName, transactionBuilder)

  fun resume(uri: String?, transactionType: AsfInAppPurchaseInteractor.TransactionType,
             packageName: String, productName: String?, developerPayload: String?,
             isBds: Boolean) =
      inAppPurchaseInteractor.resume(uri, transactionType, packageName, productName,
          developerPayload, isBds)

  fun convertToLocalFiat(appcValue: Double): Single<FiatValue> =
      inAppPurchaseInteractor.convertToLocalFiat(appcValue)

  fun hasAsyncLocalPayment() = inAppPurchaseInteractor.hasAsyncLocalPayment()

  fun hasPreSelectedPaymentMethod() = inAppPurchaseInteractor.hasPreSelectedPaymentMethod()

  fun removePreSelectedPaymentMethod() = inAppPurchaseInteractor.removePreSelectedPaymentMethod()

  fun removeAsyncLocalPayment() = inAppPurchaseInteractor.removeAsyncLocalPayment()

  fun getPaymentMethods(transaction: TransactionBuilder, transactionValue: String,
                        currency: String): Single<List<PaymentMethod>> =
      inAppPurchaseInteractor.getPaymentMethods(transaction, transactionValue, currency)

  fun mergeAppcoins(paymentMethods: List<PaymentMethod>): List<PaymentMethod> =
      inAppPurchaseInteractor.mergeAppcoins(paymentMethods)

  fun getPreSelectedPaymentMethod(): String = inAppPurchaseInteractor.preSelectedPaymentMethod

  fun getLastUsedPaymentMethod(): String = inAppPurchaseInteractor.lastUsedPaymentMethod
}