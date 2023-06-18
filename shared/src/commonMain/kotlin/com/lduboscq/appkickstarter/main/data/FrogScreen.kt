package com.lduboscq.appkickstarter.main.data

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.lduboscq.appkickstarter.FrogRepositoryRemote
import com.lduboscq.appkickstarter.FrogScreenModel
import com.lduboscq.appkickstarter.main.Image
import com.lduboscq.appkickstarter.main.book.MyBottomBar
import com.lduboscq.appkickstarter.main.book.MyTopBar
import com.lduboscq.appkickstarter.main.book.Route
import com.lduboscq.appkickstarter.main.book.getBookList
import com.lduboscq.appkickstarter.main.book.screenRouter

class FrogScreen : Screen {

    private val bookList = getBookList()

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        // Insert shopping cart repository
        val screenModel = rememberScreenModel() { FrogScreenModel(FrogRepositoryRemote()) }
        val state by screenModel.state.collectAsState()


        // Local static books data
        val bookList = getBookList()


        // Message
        var messageOnTopBar by remember { mutableStateOf("") }
        when (val result = state) {
            is FrogScreenModel.State.Init -> messageOnTopBar = "Just initialized"
            is FrogScreenModel.State.Loading -> messageOnTopBar = "Loading"
            is FrogScreenModel.State.Result -> messageOnTopBar = "Success"
            else -> {}
        }

        // Load shopping cart data
        LaunchedEffect(true) {
            screenModel.getFrog("")
        }
        var quantity by remember { mutableStateOf(0) }
        if (state is FrogScreenModel.State.Result) {
            quantity =
                (state as FrogScreenModel.State.Result).cartLineList.sumOf { frog -> frog.quantity }
        }

        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
        Scaffold(

            topBar = { MyTopBar(messageOnTopBar, scrollBehavior) },

            bottomBar = {
                MyBottomBar(
                    quantity = quantity,
                    currentScreen = Route.ShoppingCart(quantity)
                )
            },

            content = { paddingValues ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(paddingValues),
                ) {
                    if (state is FrogScreenModel.State.Result) {
                        LazyColumn {
                            for (frog in (state as FrogScreenModel.State.Result).cartLineList) {
                                item {
                                    CartLineCard(
                                        cartLine = frog,
                                        update = { screenModel.addOrUpdateFrog(it) },
                                        delete = { screenModel.deleteFrog(it) }

                                    )
                                }
                            }
                        }
                    }
                }
            },

            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        )
    }

    @Composable
    fun CartLineCard(
        cartLine: CartLine,
        update: (frog: CartLineData) -> Unit,
        delete: (id: String) -> Unit
    ) {
        val navigator = LocalNavigator.currentOrThrow
        val book = bookList.firstOrNull { cartLine.bookId == it.id }

        if (book == null) {
            Text(cartLine.quantity.toString())
            delete(cartLine._id)
        } else {
            Card(
                modifier = Modifier.size(width = 420.dp, height = 160.dp).padding(15.dp),
            ) {
                Column {
                    Text(
                        text = "${book.title}",
                        modifier = Modifier.padding(6.dp, 0.dp, 6.dp, 0.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign = TextAlign.Center
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,

                    ){
                        Image(
                            url = book.imagePath,
                            modifier = Modifier.size(width = 80.dp, height = 120.dp).padding(15.dp)
                                .clickable(onClick = {
                                    navigator.push(screenRouter(Route.Detail(book)))
                                })
                        )

                        Spacer(modifier = Modifier.width(120.dp))

                        AddOrSubstrateQuantity(
                            cartLine = cartLine,
                            update = { update(it) },
                            delete = { delete(it) })
                    }
                }
            }
        }
    }
}