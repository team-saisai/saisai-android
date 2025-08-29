package com.choius323.saisai.ui.screen.ride_history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.CheckCircleOutline
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.choius323.saisai.ui.component.EmptyCourseList
import com.choius323.saisai.ui.model.RideHistoryItem
import com.choius323.saisai.ui.theme.SaiColor
import com.choius323.saisai.ui.theme.SaiTheme

@Composable
fun RideHistoryListSection(
    courseList: List<RideHistoryItem>,
    selectedIndexList: List<Int>,
    deletedIndexList: List<Int>,
    isEditMode: Boolean,
    isLoadingMore: Boolean,
    modifier: Modifier = Modifier,
    onCourseClick: (Int) -> Unit,
    onCourseSelect: (Int) -> Unit,
    loadMore: () -> Unit,
    onClickEmptyButton: () -> Unit,
) {
    if (courseList.isEmpty()) {
        EmptyCourseList(
            content = "주행한 코스가 없습니다.",
            onClick = onClickEmptyButton,
            modifier = Modifier.fillMaxSize()
        )
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 10.dp),
        ) {
            itemsIndexed(
                items = courseList, key = { _, item -> item.rideId }
            ) { index, item ->
                if (index !in deletedIndexList) {
                    RideHistoryListItem(
                        item = item,
                        isEditMode = isEditMode,
                        isSelected = index in selectedIndexList,
                        onItemClick = {
                            if (isEditMode) {
                                onCourseSelect(index)
                            } else {
                                onCourseClick(index)
                            }
                        },
                    )
                    if (index != courseList.lastIndex) {
                        Spacer(Modifier.height(18.dp))
                    }
                }
                if (index == courseList.lastIndex) {
                    loadMore()
                }
            }

            if (isLoadingMore) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                }
            }
        }
    }
}

@Composable
private fun RideHistoryListItem(
    item: RideHistoryItem,
    isEditMode: Boolean,
    isSelected: Boolean,
    onItemClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .fillMaxWidth()
            .clickable(onClick = onItemClick),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        if (isEditMode) {
            Box(Modifier.size(28.dp), contentAlignment = Alignment.Center) {
                if (isSelected) {
                    Icon(Icons.Filled.Circle, "체크 표시 배경", Modifier.fillMaxSize(), SaiColor.Purple)
                    Icon(Icons.Rounded.Check, "체크 표시", Modifier.fillMaxSize(0.65f), SaiColor.White)
                } else {
                    Icon(
                        Icons.Rounded.CheckCircleOutline,
                        "체크 표시",
                        Modifier.fillMaxSize(),
                        SaiColor.Gray70
                    )
                }
            }
        }
        RideHistoryItem(
            course = item,
            isEditMode = isEditMode,
            modifier = Modifier.weight(1f),
        )
    }
}

@Preview
@Composable
private fun RideHistoryListSectionPreviewEmpty() {
    SaiTheme {
        RideHistoryListSection(
            emptyList(),
            emptyList(),
            emptyList(),
            isEditMode = true,
            isLoadingMore = false,
            modifier = Modifier,
            onCourseClick = {},
            onCourseSelect = {},
            loadMore = {},
            {})
    }
}