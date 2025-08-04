package com.choius323.saisai.ui.screen.bookmark_courses

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.choius323.saisai.ui.component.CourseListItemHorizontal
import com.choius323.saisai.ui.model.CourseListItem
import com.choius323.saisai.ui.theme.SaiTheme

@Composable
fun BookmarkCoursesListSection(
    courseList: List<CourseListItem>,
    selectedIndexList: List<Int>,
    isEditMode: Boolean,
    onCourseClick: (Int) -> Unit,
    onCourseSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        itemsIndexed(
            items = courseList,
            key = { _, item -> item }
        ) { index, item ->
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
                }
            )
        }
    }
}

@Composable
private fun BookmarkCourseListItem(
    item: CourseListItem,
    isEditMode: Boolean,
    isSelected: Boolean,
    onItemClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onItemClick),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        if (isEditMode) {
            Checkbox(
                checked = isSelected,
                onCheckedChange = null,
                modifier = Modifier.size(28.dp)
            )
        }
        CourseListItemHorizontal(
            course = item,
            modifier = Modifier.weight(1f),
            showBookmark = false,
            onClickBookmark = {},
        )
    }
}


@Preview
@Composable
private fun BookmarkCourseListItemPreview() {
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

