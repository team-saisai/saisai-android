package com.choius323.saisai.ui.screen.total_reward

import androidx.lifecycle.ViewModel
import com.choius323.saisai.repository.AccountRepository
import com.choius323.saisai.ui.model.TotalReward
import kotlinx.coroutines.flow.collectLatest
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

class TotalRewardViewModel(
    private val accountRepository: AccountRepository,
) : ViewModel(), ContainerHost<TotalRewardUiState, TotalRewardSideEffect> {

    override val container: Container<TotalRewardUiState, TotalRewardSideEffect> =
        container(TotalRewardUiState())

    init {
        loadTotalReward()
    }

    fun onEvent(event: TotalRewardUiEvent) = intent {
        when (event) {
            TotalRewardUiEvent.OnClickBack -> postSideEffect(TotalRewardSideEffect.GoBack)
            TotalRewardUiEvent.OnClickEmpty -> postSideEffect(TotalRewardSideEffect.GoToCourseList)
        }
    }

    private fun loadTotalReward() = intent {
        reduce { state.copy(isLoading = true, error = null) }

        accountRepository.getTotalReward().collectLatest { result ->
            result.onSuccess { totalRewardData: TotalReward ->
                reduce {
                    state.copy(
                        isLoading = false,
                        totalPoints = totalRewardData.totalReward,
                        rewardInfoList = totalRewardData.rewardInfoList
                    )
                }
            }.onFailure { exception ->
                val errorMessage = exception.message ?: "리워드 정보를 불러오는데 실패했습니다."
                reduce {
                    state.copy(
                        isLoading = false,
                        error = errorMessage
                    )
                }
                postSideEffect(TotalRewardSideEffect.ShowToast(errorMessage))
            }
        }
    }
}
