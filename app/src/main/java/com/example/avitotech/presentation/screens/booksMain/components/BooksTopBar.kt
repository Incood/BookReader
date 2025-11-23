package com.example.avitotech.presentation.screens.booksMain.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.avitotech.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BooksTopBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSortClick: () -> Unit,
    isSearchVisible: Boolean,
    onSearchVisibleChange: (Boolean) -> Unit,
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    Column {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.my_books),
                    style = MaterialTheme.typography.headlineSmall
                )
            },
            actions = {
                IconButton(onClick = { onSearchVisibleChange(!isSearchVisible) }) {
                    Icon(
                        imageVector = if (isSearchVisible) Icons.Default.Close else Icons.Default.Search,
                        contentDescription = if (isSearchVisible) stringResource(R.string.close_search) else stringResource(R.string.search),
                        modifier = Modifier.size(24.dp)
                    )
                }

                if (!isSearchVisible) {
                    IconButton(onClick = onSortClick) {
                        Icon(
                            painter = painterResource(R.drawable.ic_sort),
                            contentDescription = stringResource(R.string.sorting),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            },
            scrollBehavior = scrollBehavior
        )
    }
}