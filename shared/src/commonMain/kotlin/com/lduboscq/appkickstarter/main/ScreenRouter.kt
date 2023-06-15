package com.lduboscq.appkickstarter.main

import cafe.adriel.voyager.core.screen.Screen
import com.lduboscq.appkickstarter.main.screen.AboutScreen
import com.lduboscq.appkickstarter.main.screen.BookStoreHomeScreen
import com.lduboscq.appkickstarter.main.screen.DetailScreen
import com.lduboscq.appkickstarter.main.screen.ShoppingCartScreen

fun screenRouter(screen: Route): Screen {
    return when (screen) {
        is Route.Home -> {
            BookStoreHomeScreen(user = screen.user)
        }

        is Route.About -> {
            if (screen.count == 0) {
                AboutScreen(message = "Welcome")
            } else {
                AboutScreen(message = "Welcome Back")
            }
        }

        is Route.Detail -> {
            DetailScreen(book= screen.book)
        }

        is Route.ShoppingCart -> {
            ShoppingCartScreen(count = screen.count)
        }
    }
}