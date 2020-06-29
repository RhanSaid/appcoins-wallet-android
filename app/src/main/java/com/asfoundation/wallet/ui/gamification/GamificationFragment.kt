package com.asfoundation.wallet.ui.gamification

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appcoins.wallet.gamification.LevelViewModel
import com.asf.wallet.R
import com.asfoundation.wallet.analytics.gamification.GamificationAnalytics
import com.asfoundation.wallet.ui.widget.MarginItemDecoration
import com.asfoundation.wallet.util.CurrencyFormatUtils
import dagger.android.support.DaggerFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_gamification.*
import java.math.BigDecimal
import javax.inject.Inject

class GamificationFragment : DaggerFragment(), GamificationView {

  @Inject
  lateinit var interactor: GamificationInteractor

  @Inject
  lateinit var analytics: GamificationAnalytics

  @Inject
  lateinit var formatter: CurrencyFormatUtils
  private lateinit var presenter: GamificationPresenter
  private lateinit var activityView: RewardsLevelView
  private lateinit var levelsAdapter: LevelsAdapter
  private var uiEventListener: PublishSubject<Boolean>? = null

  override fun onAttach(context: Context) {
    super.onAttach(context)
    require(
        context is RewardsLevelView) { GamificationFragment::class.java.simpleName + " needs to be attached to a " + RewardsLevelView::class.java.simpleName }
    activityView = context
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    uiEventListener = PublishSubject.create()
    presenter =
        GamificationPresenter(this, activityView, interactor, analytics, formatter,
            CompositeDisposable(), AndroidSchedulers.mainThread(), Schedulers.io())
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_gamification, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    presenter.present(savedInstanceState)
  }

  override fun displayGamificationInfo(currentLevel: Int, levels: List<LevelViewModel>,
                                       totalSpend: BigDecimal) {
    val layoutManager = LinearLayoutManager(context)
    layoutManager.orientation = RecyclerView.VERTICAL
    levelsAdapter = LevelsAdapter(context!!, levels, totalSpend, currentLevel,
        uiEventListener!!)
    gamification_recycler_view.addItemDecoration(
        MarginItemDecoration(resources.getDimension(R.dimen.wallets_card_margin)
            .toInt()))
    gamification_recycler_view.layoutManager = layoutManager
    gamification_recycler_view.adapter = levelsAdapter
  }

  override fun showHeaderInformation(totalSpent: String, bonusEarned: String, symbol: String) {
    bonus_earned.text = getString(R.string.value_fiat, symbol, bonusEarned)
    total_spend.text = getString(R.string.gamification_how_table_a2, totalSpent)

    bonus_earned_skeleton.visibility = View.INVISIBLE
    total_spend_skeleton.visibility = View.INVISIBLE
    bonus_earned.visibility = View.VISIBLE
    total_spend.visibility = View.VISIBLE
  }

  override fun getLevelsClicks() = uiEventListener!!

  override fun toogleReachedLevels(hide: Boolean) {
    levelsAdapter.toogleReachedLevels(hide)
    if (hide.not()) gamification_recycler_view.scrollToPosition(0)
  }


  override fun onDestroyView() {
    presenter.stop()
    super.onDestroyView()
  }
}