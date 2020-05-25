package com.appcoins.wallet.bdsbilling

import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*

interface SubscriptionBillingService {

  @GET("product/8.20200301/inapp/{domain}")
  fun getPackage(@Path("domain") packageName: String): Single<SellerDomain>

  @GET("product/8.20200301/inapp/{domain}/subscriptions")
  fun getSubscriptions(@Path("domain") domain: String,
                       @Query("skus") skus: List<String>?,
                       @Query("cursor") cursor: Long? = null,
                       @Query("limit") limit: Long? = null): Single<SubscriptionsResponse>

  @GET("product/8.20200301/inapp/{domain}/subscriptions/{sku}")
  fun getSkuSubscription(@Path("domain") domain: String,
                         @Path("sku") sku: String): Single<SubscriptionResponse>

  @GET("product/8.20200301/inapp/{domain}/subscriptions/{sku}/token")
  fun getSkuSubscriptionToken(@Path("domain") domain: String,
                              @Path("sku") sku: String,
                              @Query("currency") currency: String?): Single<String>

  @GET("product/8.20200301/inapp/{domain}/subscription/purchases")
  fun getPurchases(@Path("domain") domain: String,
                   @Query("cursor") cursor: Long? = null,
                   @Query("limit") limit: Long? = null): Single<SubscriptionPurchasListResponse>

  @GET("product/8.20200301/inapp/{domain}/subscription/purchases/{uid}")
  fun getPurchase(@Path("domain") domain: String,
                  @Path("uid") uid: String): Single<SubscriptionPurchaseResponse>

  @PATCH("product/8.20200301/inapp/{domain}/subscription/purchases/{uid}")
  fun updatePurchase(@Path("domain") domain: String,
                     @Path("uid") uid: String,
                     @Body purchaseUpdate: PurchaseUpdate): Completable

}