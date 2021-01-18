package com.optic.projectofinal.retrofit;

import com.optic.projectofinal.modelsRetrofit.JobsQueryModel;
import com.optic.projectofinal.modelsRetrofit.WorkerQueryModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FunctionsApi {

    @GET("professional/{category}/{own_id}")
    Call<List<WorkerQueryModel>> professionals(@Path("category") Integer category, @Path("own_id") String id, @Query("orderby") Integer orderby, @Query("price_start") Double price_start, @Query("price_end") Double price_end, @Query("last_conexion") Integer last_conexion);

    @GET("professional/-1/{own_id}")
    Call<List<WorkerQueryModel>> getAllProfessionals(@Path("own_id") String id);

    @GET("jobs/averagePrice/{id_job}")
    Call<JobsQueryModel> jobs(@Path("id_job") String id);
}
