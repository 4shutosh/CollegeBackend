package com.collegeapp.routes

import com.collegeapp.auth.JwtService.JwtData
import com.collegeapp.data.repositories.UserRepository
import com.collegeapp.models.ServerResponse
import com.collegeapp.utils.Constants
import com.collegeapp.utils.Constants.EndPoints.ROUTE_LOGIN
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

object LoginRoute {

    fun Route.loginOrCreateUser(jwtData: JwtData) {

        val userRepository: UserRepository by inject()

        post("/$ROUTE_LOGIN") {

            val userEmail = call.parameters[Constants.EMAIL]
            val userName = call.parameters[Constants.NAME]
            val userImageUrl = call.parameters[Constants.IMAGE_URL].toString()

            if (userEmail.isNullOrEmpty() || userName.isNullOrEmpty() || userImageUrl.isEmpty()) {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            val loggedInUser = userRepository.login(jwtData, userEmail, userName, userImageUrl)

            call.respond(
                ServerResponse(
                    data = loggedInUser,
                    status = HttpStatusCode.OK.value,
                    message = "User Logged in"
                )
            )
            return@post
        }
    }

}