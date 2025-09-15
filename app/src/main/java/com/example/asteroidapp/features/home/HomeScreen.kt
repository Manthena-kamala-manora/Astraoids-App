package com.example.asteroidapp.features.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.asteroidapp.R
import com.example.asteroidapp.api.models.AsteroidModel
import com.example.asteroidapp.api.models.ImageOfTodayModel
import com.example.asteroidapp.data.repository.AsteroidRepository
import com.example.asteroidapp.data.repository.AsteroidRepository.Companion.fakeAsteroidsList
import com.example.asteroidapp.features.main.view.AsteroidAppTopBar
import com.example.asteroidapp.features.main.viewModel.AsteroidUiState
import com.example.asteroidapp.features.main.viewModel.MainViewModel
import com.example.asteroidapp.navigation.NavigationDestination
import com.example.asteroidapp.theme.md_theme_light_scrim
import com.example.asteroidapp.util.dimenToSp
import kotlinx.coroutines.flow.flowOf
import org.koin.androidx.compose.koinViewModel


object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = koinViewModel(),
    navigateToItemDetail: (asteroidModel: AsteroidModel) -> Unit,
) {
    val asteroidUiState = viewModel.asteroidUiState
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AsteroidAppTopBar(
                title = stringResource(HomeDestination.titleRes),
                canNavigateBack = false,
                scrollBehavior = scrollBehavior,
                onFilterClick = { filter ->
                    viewModel.updateFilter(filter)
                },
            )
        },
    ) { innerPadding ->
        when (asteroidUiState) {

            is AsteroidUiState.Loading -> {
                LoadingScreen(
                    modifier = modifier
                        .fillMaxSize()
                        .fillMaxHeight()
                )
            }

            is AsteroidUiState.Success -> {
                val asteroidPagingItems =
                    asteroidUiState.asteroidModelList?.collectAsLazyPagingItems()
                val imageOfTodayModel = asteroidUiState.imageOfToday
                HomeBody(
                    itemList = asteroidPagingItems,
                    imageOfTodayModel = imageOfTodayModel,
                    onItemClick = navigateToItemDetail,
                    modifier = modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    contentPadding = innerPadding
                )
            }

            is AsteroidUiState.Error -> {

            }
        }
    }
}

@Composable
private fun HomeBody(
    modifier: Modifier = Modifier,
    itemList: LazyPagingItems<AsteroidModel>? = null,
    imageOfTodayModel: ImageOfTodayModel? = null,
    onItemClick: (AsteroidModel) -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(), contentPadding = contentPadding
    ) {
        // Header item (Image of Today)
        imageOfTodayModel?.let { imageModel ->
            item {
                HomeHeader(imageOfTodayModel = imageModel, modifier = modifier)
            }
        }
        // Show no data message if the list is empty and loading state is active
        itemList?.let { list ->
            if (list.loadState.refresh !is LoadState.Loading && list.itemCount == 0) {
                item {
                    HomeNoDataMessage()
                }
            } else {
                // List items
                items(list.itemCount) { index ->
                    list[index]?.let { asteroid ->
                        AsteroidItem(asteroidModel = asteroid,
                            modifier = Modifier.clickable { onItemClick(asteroid) })
                    }
                }
            }
        }
    }
}

@Composable
private fun ImageOfToday(modifier: Modifier = Modifier, imageOfTodayModel: ImageOfTodayModel) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp)
            .background(color = md_theme_light_scrim)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current).data(imageOfTodayModel.url)
                .crossfade(true).build(),
            error = painterResource(R.drawable.ic_broken_image),
            placeholder = painterResource(R.drawable.loading_img),
            contentDescription = imageOfTodayModel.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = md_theme_light_scrim)
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .background(color = md_theme_light_scrim),
        ) {
            Text(
                text = imageOfTodayModel.title,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 2,
                color = Color.White,
                lineHeight = 26.sp,
                modifier = Modifier
                    .padding(
                        start = dimensionResource(R.dimen.dim_default_margin),
                        end = dimensionResource(R.dimen.dim_default_margin),
                        top = dimensionResource(R.dimen.dim_default_margin),
                        bottom = dimensionResource(R.dimen.dim_default_margin)
                    )
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
private fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.background(color = md_theme_light_scrim)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(R.drawable.loading_img).crossfade(true).build(),
            contentDescription = stringResource(R.string.text_description_loading_image),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = md_theme_light_scrim)
        )
    }
}

@Composable
private fun AsteroidItem(
    modifier: Modifier = Modifier, asteroidModel: AsteroidModel
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.dim_default_margin)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Name and date
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = dimensionResource(R.dimen.dim_small_margin))
        ) {
            Text(
                text = asteroidModel.codename,
                fontSize = dimenToSp(R.dimen.text_normal),
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dim_4dp)))
            Text(
                text = asteroidModel.closeApproachDate,
                fontSize = dimenToSp(R.dimen.text_small),
                color = Color.Gray,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Hazardous status icon and description
        var hazardousDescription: String = ""
        var iconRes: Int = 0

        if (asteroidModel.isPotentiallyHazardous) {
            iconRes = R.drawable.ic_status_potentially_hazardous // Replace with your hazardous icon
            hazardousDescription =
                stringResource(R.string.text_description_potentially_hazardous_asteroid_image)
        } else {
            iconRes = R.drawable.ic_status_normal // Replace with your normal icon
            hazardousDescription =
                stringResource(R.string.text_description_not_hazardous_asteroid_image)
        }

        Image(
            painter = painterResource(id = iconRes),
            contentDescription = hazardousDescription,
            modifier = Modifier.size(24.dp),
            contentScale = ContentScale.Fit
        )
    }
}

@Preview
@Composable
fun PreviewAsteroidItem() {
    AsteroidItem(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black),
        asteroidModel = AsteroidRepository.getDummyModel()
    )
}

@Preview(showBackground = true)
@Composable
private fun ImageOfTodayPreview() {
    ImageOfToday(
        modifier = Modifier.background(Color.Black), imageOfTodayModel = ImageOfTodayModel()
    )
}

@Preview(showBackground = true)
@Composable
private fun HomeBodyPreview() {
    HomeBody(
        modifier = Modifier
            .background(Color.Black)
            .fillMaxSize(),
        imageOfTodayModel = ImageOfTodayModel(),
        onItemClick = {},
        itemList = fakeLazyPagingItems(fakeAsteroidsList)
    )
}

@Composable
private fun HomeHeader(modifier: Modifier = Modifier, imageOfTodayModel: ImageOfTodayModel) {
    ImageOfToday(
        imageOfTodayModel = imageOfTodayModel, modifier = modifier.fillMaxWidth()
    )
}

@Composable
private fun HomeNoDataMessage(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(R.string.no_data),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.dim_default_margin))
    )
}

@Composable
fun <T : Any> fakeLazyPagingItems(data: List<T>): LazyPagingItems<T> {
    val fakeFlow = remember { flowOf(PagingData.from(data)) }
    val pagingData = fakeFlow.collectAsLazyPagingItems()
    return pagingData
}