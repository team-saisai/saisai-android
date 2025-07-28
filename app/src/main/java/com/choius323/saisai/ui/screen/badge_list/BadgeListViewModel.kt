package com.choius323.saisai.ui.screen.badge_list

import androidx.lifecycle.ViewModel
import com.choius323.saisai.repository.AccountRepository
import kotlinx.coroutines.flow.collectLatest
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

class BadgeListViewModel(
    private val accountRepository: AccountRepository,
) : ViewModel(), ContainerHost<BadgeListUiState, BadgeListSideEffect> {
    override val container: Container<BadgeListUiState, BadgeListSideEffect> =
        container(BadgeListUiState())

    init {
        loadData()
    }

    fun onEvent(event: BadgeListUiEvent) = when (event) {
        BadgeListUiEvent.CloseBadgeDialog -> closeBadgeDialog()
        is BadgeListUiEvent.OnClickBadge -> onClickBadge(event.badgeId)
        BadgeListUiEvent.OnClickBack -> intent {
            postSideEffect(BadgeListSideEffect.GoBack)
        }
    }

    private fun loadData() = intent {
        if (state.badgeList.isNotEmpty()) return@intent
        reduce { state.copy(isLoading = true) }
        accountRepository.getUserBadgeList().collectLatest { result ->
            result.onSuccess { badgeList ->
                reduce {
                    state.copy(badgeList = badgeList, isLoading = false)
                }
            }.onFailure {
                reduce {
                    state.copy(isLoading = false)
                }
                postSideEffect(BadgeListSideEffect.ShowToast(it.message ?: "배지 목록 조회에 실패했습니다."))
            }
        }
    }

    private fun closeBadgeDialog() = intent {
        reduce {
            state.copy(showBadgeDetail = null)
        }
    }

    private fun onClickBadge(badgeId: Long) = intent {
        reduce {
            state.copy(isLoading = true)
        }
        accountRepository.getUserBadgeDetail(badgeId).collectLatest { result ->
            result.onSuccess { badgeDetail ->
                reduce {
                    state.copy(showBadgeDetail = badgeDetail, isLoading = false)
                }
            }.onFailure {
                reduce {
                    state.copy(isLoading = false)
                }
                postSideEffect(BadgeListSideEffect.ShowToast(it.message ?: "배지 상세 조회에 실패했습니다."))
            }
        }
    }
}