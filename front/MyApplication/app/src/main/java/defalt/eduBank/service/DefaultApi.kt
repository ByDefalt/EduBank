package defalt.eduBank.service

import retrofit2.http.*
import retrofit2.Response

import com.example.api.model.Account
import com.example.api.model.JwtToken

interface DefaultApi {
    /**
     * DELETE account/{id}
     * Delete an account by ID
     * 
     * Responses:
     *  - 204: Account deleted
     *  - 404: Account not found
     *
     * @param id 
     * @return [Unit]
     */
    @DELETE("account/{id}")
    suspend fun accountIdDelete(@Path("id") id: Int): Response<Unit>

    /**
     * GET account/{id}
     * Get an account by ID
     * 
     * Responses:
     *  - 200: Account found
     *  - 404: Account not found
     *
     * @param id 
     * @return [Account]
     */
    @GET("account/{id}")
    suspend fun accountIdGet(@Path("id") id: Int): Response<Account>

    /**
     * PUT account/{id}
     * Update an account by ID
     * 
     * Responses:
     *  - 200: Account updated
     *  - 400: Bad request
     *  - 404: Account not found
     *
     * @param id 
     * @param account 
     * @return [Account]
     */
    @PUT("account/{id}")
    suspend fun accountIdPut(@Path("id") id: Int, @Body account: Account): Response<Account>

    /**
     * POST account
     * Create a new account
     * 
     * Responses:
     *  - 201: Account created
     *  - 400: Bad request
     *
     * @param account 
     * @return [Account]
     */
    @POST("account")
    suspend fun accountPost(@Body account: Account): Response<Account>

    /**
     * POST account/signin
     * Sign in and get authentication token
     * 
     * Responses:
     *  - 200: Authentication successful
     *  - 401: Invalid credentials
     *
     * @param account 
     * @return [JwtToken]
     */
    @POST("account/signin")
    suspend fun accountSigninPost(@Body account: Account): Response<JwtToken>

    /**
     * POST account/validate
     * Validate authentication token
     * 
     * Responses:
     *  - 200: Token validation result
     *
     * @param jwtToken 
     * @return [Boolean]
     */
    @POST("account/validate")
    suspend fun accountValidatePost(@Body jwtToken: JwtToken): Response<Boolean>

}
