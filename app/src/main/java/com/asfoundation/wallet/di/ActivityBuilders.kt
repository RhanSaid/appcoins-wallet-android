package com.asfoundation.wallet.di

import com.asfoundation.wallet.advertise.AdvertisingService
import com.asfoundation.wallet.permissions.request.view.PermissionsActivity
import com.asfoundation.wallet.referrals.InviteFriendsActivity
import com.asfoundation.wallet.topup.TopUpActivity
import com.asfoundation.wallet.ui.*
import com.asfoundation.wallet.ui.backup.WalletBackupActivity
import com.asfoundation.wallet.ui.balance.QrCodeActivity
import com.asfoundation.wallet.ui.balance.RestoreWalletActivity
import com.asfoundation.wallet.ui.balance.TokenDetailsActivity
import com.asfoundation.wallet.ui.balance.TransactionDetailActivity
import com.asfoundation.wallet.ui.iab.IabActivity
import com.asfoundation.wallet.ui.iab.WebViewActivity
import com.asfoundation.wallet.ui.onboarding.OnboardingActivity
import com.asfoundation.wallet.wallet_blocked.WalletBlockedActivity
import com.asfoundation.wallet.wallet_validation.generic.WalletValidationActivity
import com.asfoundation.wallet.wallet_validation.poa.PoaWalletValidationActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilders {

  @ActivityScope
  @ContributesAndroidInjector
  internal abstract fun bindSplashModule(): SplashActivity

  @ActivityScope
  @ContributesAndroidInjector
  internal abstract fun bindBaseActivityModule(): BaseActivity

  @ActivityScope
  @ContributesAndroidInjector(modules = [TransactionsModule::class])
  internal abstract fun bindTransactionsModule(): TransactionsActivity

  @ActivityScope
  @ContributesAndroidInjector(modules = [TransactionDetailModule::class])
  internal abstract fun bindTransactionDetailModule(): TransactionDetailActivity

  @ActivityScope
  @ContributesAndroidInjector
  internal abstract fun bindSettingsModule(): SettingsActivity

  @ActivityScope
  @ContributesAndroidInjector(modules = [SendModule::class])
  internal abstract fun bindSendModule(): SendActivity

  @ActivityScope
  @ContributesAndroidInjector(modules = [MyAddressModule::class])
  internal abstract fun bindMyAddressModule(): MyAddressActivity

  @ActivityScope
  @ContributesAndroidInjector
  internal abstract fun bindPermissionsActivity(): PermissionsActivity

  @ActivityScope
  @ContributesAndroidInjector(modules = [ConfirmationModule::class])
  internal abstract fun bindConfirmationModule(): ConfirmationActivity

  @ActivityScope
  @ContributesAndroidInjector
  internal abstract fun bindIabModule(): IabActivity

  @ActivityScope
  @ContributesAndroidInjector(modules = [GasSettingsModule::class])
  internal abstract fun bindGasSettingsModule(): GasSettingsActivity

  @ActivityScope
  @ContributesAndroidInjector
  internal abstract fun bindTopUpActivity(): TopUpActivity

  @ActivityScope
  @ContributesAndroidInjector
  internal abstract fun bindOnboardingModule(): OnboardingActivity

  @ActivityScope
  @ContributesAndroidInjector
  internal abstract fun bindInviteFriendsActivity(): InviteFriendsActivity

  @ActivityScope
  @ContributesAndroidInjector
  internal abstract fun bindActiveWalletActivity(): QrCodeActivity

  @ContributesAndroidInjector
  internal abstract fun bindWebViewActivity(): WebViewActivity

  @ActivityScope
  @ContributesAndroidInjector
  internal abstract fun bindPoaWalletValidationActivity(): PoaWalletValidationActivity

  @ActivityScope
  @ContributesAndroidInjector
  internal abstract fun bindUpdateRequiredActivity(): UpdateRequiredActivity

  @ContributesAndroidInjector
  internal abstract fun bindTokenDetailsFragment(): TokenDetailsActivity

  @ActivityScope
  @ContributesAndroidInjector
  internal abstract fun bindWalletValidationActivity(): WalletValidationActivity

  @ActivityScope
  @ContributesAndroidInjector
  internal abstract fun bindWalletBlockedActivity(): WalletBlockedActivity

  @ActivityScope
  @ContributesAndroidInjector
  internal abstract fun bindRestoreWalletActivity(): RestoreWalletActivity

  @ActivityScope
  @ContributesAndroidInjector
  internal abstract fun bindWalletBackupActivity(): WalletBackupActivity

  @ActivityScope
  @ContributesAndroidInjector
  abstract fun bindErc681Receiver(): Erc681Receiver

  @ActivityScope
  @ContributesAndroidInjector
  abstract fun bindOneStepPaymentReceiver(): OneStepPaymentReceiver

  @ActivityScope
  @ContributesAndroidInjector
  abstract fun bindAdvertisingService(): AdvertisingService

}