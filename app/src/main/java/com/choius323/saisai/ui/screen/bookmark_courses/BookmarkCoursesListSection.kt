package com.choius323.saisai.ui.screen.bookmark_courses

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.choius323.saisai.ui.component.CourseListItemHorizontal
import com.choius323.saisai.ui.component.EmptyCourseList
import com.choius323.saisai.ui.model.CourseListItem
import com.choius323.saisai.ui.theme.SaiColor
import com.choius323.saisai.ui.theme.SaiTheme
import com.jakewharton.threetenabp.AndroidThreeTen

@Composable
fun BookmarkCoursesListSection(
    courseList: List<CourseListItem>,
    selectedIndexList: List<Int>,
    deletedIndexList: List<Int>,
    isEditMode: Boolean,
    isLoadingMore: Boolean,
    onCourseClick: (Int) -> Unit,
    onCourseSelect: (Int) -> Unit,
    loadMore: () -> Unit,
    onClickBookmark: (Int) -> Unit,
    onClickEmptyButton: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (courseList.isEmpty()) {
        EmptyCourseList(
            content = "저장한 코스가 없습니다.\n" +
                    "코스를 탐색해 나의 취향을 발견해보세요.",
            onClick = onClickEmptyButton,
            modifier = Modifier.fillMaxSize()
        )
    } else {
        LazyColumn(
            modifier = modifier
                .padding(horizontal = 18.dp)
                .fillMaxSize(),
            contentPadding = PaddingValues(vertical = 10.dp)
        ) {

            itemsIndexed(
                items = courseList,
                key = { _, item -> item.courseId }
            ) { index, item ->
                if (index !in deletedIndexList) {
                    BookmarkCourseListItem(
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
                        onClickBookmark = { onClickBookmark(index) }
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
private fun BookmarkCourseListItem(
    item: CourseListItem,
    isEditMode: Boolean,
    isSelected: Boolean,
    onItemClick: () -> Unit,
    onClickBookmark: () -> Unit = {},
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
        CourseListItemHorizontal(
            course = item,
            modifier = Modifier.weight(1f),
            showBookmark = isEditMode.not(),
            onClickBookmark = onClickBookmark,
        )
    }
}


@Preview
@Composable
private fun BookmarkCourseListItemPreview() {
    AndroidThreeTen.init(LocalContext.current)
    SaiTheme {
        Surface {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                BookmarkCourseListItem(
                    item = CourseListItem.dummyItem1,
                    isEditMode = true,
                    isSelected = true,
                    onItemClick = {})
                BookmarkCourseListItem(
                    item = CourseListItem.dummyItem1,
                    isEditMode = true,
                    isSelected = false,
                    onItemClick = {})
                BookmarkCourseListItem(
                    item = CourseListItem.dummyItem1,
                    isEditMode = false,
                    isSelected = true,
                    onItemClick = {})
            }
        }
    }
}

