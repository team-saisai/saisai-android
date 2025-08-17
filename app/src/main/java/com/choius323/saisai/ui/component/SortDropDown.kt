package com.choius323.saisai.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.choius323.saisai.ui.model.CourseSort
import com.choius323.saisai.ui.model.CourseType
import com.choius323.saisai.ui.screen.course_list.CourseListHead
import com.choius323.saisai.ui.theme.SaiColor
import com.choius323.saisai.ui.theme.SaiTheme

@Composable
fun SortDropDown(
    sortList: List<CourseSort>,
    selected: CourseSort,
    onSelectedSort: (CourseSort) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(SaiColor.White.copy(alpha = 0.05f))
                .clickable { expanded = expanded.not() }
                .padding(start = 12.dp, end = 4.dp, top = 5.dp, bottom = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SaiText(
                text = selected.displayName,
                color = SaiColor.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.W400,
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Sort options",
                tint = SaiColor.White,
                modifier = Modifier
                    .size(20.dp)
                    .rotate(if (expanded) 180f else 0f)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            border = BorderStroke(1.dp, SaiColor.Gray40),
            shape = RoundedCornerShape(8.dp),
            modifier = modifier
                .crop(vertical = 8.dp)
                .width(IntrinsicSize.Min)
                .background(SaiColor.Gray85)
                .padding(4.dp),
            offset = DpOffset(x = 0.dp, y = 5.dp)
        ) {
            sortList.forEachIndexed { index, item ->
                CourseSortDropDownItem(
                    sort = item,
                    isSelected = index == CourseSort.valueOf(selected.name).ordinal,
                    modifier = Modifier,
                    onSelect = {
                        onSelectedSort(item)
                        expanded = false
                    }
                )
            }
        }
    }
}


@Composable
private fun ColumnScope.CourseSortDropDownItem(
    sort: CourseSort,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onSelect: () -> Unit,
) {
    val selectedColor = remember { Color(0xFFB0A2FF) }
    SaiText(
        text = sort.displayName,
        modifier = modifier
            .align(Alignment.CenterHorizontally)
            .clip(RoundedCornerShape(4.dp))
            .clickable(onClick = onSelect)
            .let { if (isSelected) it.background(selectedColor.copy(0.1f)) else it }
            .padding(PaddingValues(horizontal = 14.dp, vertical = 8.dp)),
        textAlign = TextAlign.Center,
        fontSize = 13.sp,
        fontWeight = FontWeight.W400,
        color = if (isSelected) selectedColor else SaiColor.White,
    )
}

@Preview
@Composable
private fun CourseListHeadPreview() {
    SaiTheme {
        Surface {
            CourseListHead(
                CourseType.challenge,
                CourseSort.levelAsc,
                {},
                {},
                Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF202326)
@Composable
private fun CourseSortDropDownPreview() {
    SaiTheme {
        Box(
            Modifier
                .padding(12.dp)
        ) {
            SortDropDown(
                sortList = listOf(CourseSort.levelAsc, CourseSort.endSoon, CourseSort.newest),
                selected = CourseSort.endSoon,
                onSelectedSort = {},
            )
        }
    }
}


@Preview
@Composable
private fun CourseSortDropDownItemPreview() {
    SaiTheme {
        Surface {
            Column(
                Modifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CourseSortDropDownItem(CourseSort.endSoon, isSelected = true, onSelect = {})
                CourseSortDropDownItem(CourseSort.levelAsc, isSelected = false, onSelect = {})
            }
        }
    }
}