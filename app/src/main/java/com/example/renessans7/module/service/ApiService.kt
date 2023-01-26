package com.example.renessans7.module.service

import com.example.renessans7.models.BaseResponse
import com.example.renessans7.models.group.AddGroupRequest
import com.example.renessans7.models.group.Group
import com.example.renessans7.models.login.LoginRequest
import com.example.renessans7.models.login.AuthResponse
import com.example.renessans7.models.pupils.Pupil
import com.example.renessans7.models.register.RegisterRequest
import com.example.renessans7.models.requests.RequestsToJoin
import com.example.renessans7.models.teacher.Teacher
import com.example.renessans7.models.test.Test
import com.example.renessans7.models.test.TestCheckRequest
import com.example.renessans7.models.test.TestCheckResponse
import com.example.renessans7.models.test.TestFileUploadRequest
import okhttp3.MultipartBody
import retrofit2.http.*

interface ApiService {

    @POST("login")
    suspend fun login(@Body loginRequest: LoginRequest): BaseResponse<AuthResponse>

    @POST("register/teacher")
    suspend fun registerAsTeacher(@Body registerRequest: RegisterRequest): BaseResponse<AuthResponse>

    @POST("register/pupil")
    suspend fun registerAsPupil(@Body registerRequest: RegisterRequest): BaseResponse<AuthResponse>

    @GET("teacher")
    suspend fun getAllTeachers(): BaseResponse<List<Teacher>>

    @GET("class")
    suspend fun getTeacherGroups(@Query("teacherId1") teacherId: String): BaseResponse<List<Group>>

    @GET("class")
    suspend fun getTeacherGroups(): BaseResponse<List<Group>>

    @POST("request")
    suspend fun joinToGroup(@Query("classId") classId: String): BaseResponse<Any>

    @GET("test")
    suspend fun getGroupTests(@Query("classId") groupId: String): BaseResponse<List<Test>>

    @GET("test/all")
    suspend fun getAllTests(): BaseResponse<List<Test>>

    @Multipart
    @POST("test")
    suspend fun uploadTestFile(
        @Part("dto") testFileUploadRequest: TestFileUploadRequest, @Part file: MultipartBody.Part
    ): BaseResponse<Test>

    @POST("class")
    suspend fun addGroup(@Body groupRequest: AddGroupRequest): BaseResponse<Group>

    @GET("request/teacher")
    suspend fun getRequests(): BaseResponse<List<RequestsToJoin>>

    @PUT("request/accept")
    suspend fun acceptRequest(@Query("requestId") requestId: String): BaseResponse<Any>

    @PUT("request/decline")
    suspend fun declineRequest(@Query("requestId") requestId: String): BaseResponse<Any>

    @GET("class/user")
    suspend fun getJoinedGroups(): BaseResponse<List<Group>>

    @POST("test/forward")
    suspend fun forwardTest(
        @Query("testId") testId: String, @Query("classId") classId: String
    ): BaseResponse<Any>

    @GET("test/one")
    suspend fun getTestById(@Query("testId") testId: String): BaseResponse<Test>

    @POST("result/check")
    suspend fun check(@Body testCheckRequest: TestCheckRequest): BaseResponse<TestCheckResponse>

    @GET("result/all")
    suspend fun getGroupTestResult(
        @Query("testId") testId: String,
        @Query("classId") classId: String
    ): BaseResponse<ArrayList<TestCheckResponse>>

    @GET("request")
    suspend fun getPupilPendingRequest(): BaseResponse<List<RequestsToJoin>>

    @DELETE("request")
    suspend fun cancel(@Query("requestId") requestId: String): BaseResponse<Any>

    @GET("class/byId")
    suspend fun getGroupDataAndMembers(@Query("classId") groupId: String): BaseResponse<com.example.renessans7.models.pupils.Class>

    @DELETE("class/user")
    suspend fun removePupilFromGroup(
        @Query("classId") classId: String,
        @Query("pupilId") pupilId: String
    ): BaseResponse<Any>

    @GET("pupil")
    suspend fun getAllPupils(): BaseResponse<List<Pupil>>

    @PUT("stranger")
    suspend fun removeUser(@Query("userId") pupilId: String): BaseResponse<Any>

    @PUT("class")
    suspend fun editGroup(
        @Body editGroupRequest: AddGroupRequest,
        @Query("classId") classId: String
    ): BaseResponse<Any>

    @PUT("test/makeVisible")
    suspend fun updateTestVisibility(
        @Query("testId") testId: String,
        @Query("classId") classId: String,
        @Query("visible") visible: Boolean
    ): BaseResponse<Any>
}