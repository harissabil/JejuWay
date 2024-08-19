package com.holidai.jejuway.data.supabase.database

import com.holidai.jejuway.data.Resource
import com.holidai.jejuway.data.network.upstage_ai.dto.Itinerary
import com.holidai.jejuway.data.network.upstage_ai.dto.ScheduleItem
import com.holidai.jejuway.data.network.upstage_ai.dto.TestingLlmResponse
import com.holidai.jejuway.data.supabase.SupabaseClient
import com.holidai.jejuway.data.supabase.auth.SupabaseAuth
import com.holidai.jejuway.domain.models.itinerary.Review
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.filter.FilterOperation
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import io.github.jan.supabase.realtime.selectAsFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.selects.select
import timber.log.Timber
import java.util.UUID

class SupabaseDatabase(
    private val supabaseClient: SupabaseClient,
    private val supabaseAuth: SupabaseAuth,
) {
    suspend fun getAllItinerary(): Resource<List<TestingLlmResponse>> {
        return try {
            val userId = supabaseAuth.getUserId().data

            val colums = Columns.raw(
                """
                *,
                schedule(
                    id,
                    itinerary_id,
                    trip_day,
                    date,
                    activity_name,
                    address,
                    place_name,
                    activity_category,
                    completed,
                    photo_url,
                    details,
                    time,
                    lat,
                    long,
                    order,
                    rating,
                    review
                )
            """.trimIndent()
            )
            val response = supabaseClient
                .client
                .postgrest
                .from("itinerary")
                .select(columns = colums, request = {
                    filter {
                        eq("user_id", userId!!)
                    }
                })
                .decodeList<TestingLlmResponse>()

            return Resource.Success(response)
        } catch (e: Exception) {
            Timber.tag("SupabaseDatabaseError").e(e)
            Resource.Error(e.message)
        }
    }

    suspend fun insertItinerary(
        userId: String,
        itinerary: Itinerary,
    ): Resource<Itinerary> {
        return try {
            val itineraryToInsert = itinerary.copy(
                itineraryId = null,
                userId = userId
            )

            val response = supabaseClient
                .client
                .postgrest
                .from("itinerary")
                .insert(itineraryToInsert) {
                    select()
                }
                .decodeSingle<Itinerary>()

            return Resource.Success(response)
        } catch (e: Exception) {
            Timber.tag("SupabaseDatabaseError").e(e)
            Resource.Error(e.message)
        }
    }

    suspend fun insertAllSchedule(schedules: List<ScheduleItem>): Resource<Boolean> {
        return try {
            val schedulesToInsert = schedules.map { schedule ->
                schedule.copy(
                    id = UUID.randomUUID().toString()
                )
            }

            val response = supabaseClient
                .client
                .postgrest
                .from("schedule")
                .insert(schedulesToInsert)

            return Resource.Success(true)
        } catch (e: Exception) {
            Timber.tag("SupabaseDatabaseError").e(e)
            Resource.Error(e.message)
        }
    }

    suspend fun updateItinerary(itinerary: Itinerary): Resource<Itinerary> {
        return try {
            val response = supabaseClient
                .client
                .postgrest
                .from("itinerary")
                .update(itinerary) {
                    select { }
                }.decodeSingle<Itinerary>()

            return Resource.Success(response)
        } catch (e: Exception) {
            Timber.tag("SupabaseDatabaseError").e(e)
            Resource.Error(e.message)
        }
    }

    suspend fun updateSchedule(
        itineraryId: String,
        schedules: List<ScheduleItem>,
    ): Resource<Boolean> {
        return try {
            val deleteAllResponse = supabaseClient
                .client
                .postgrest
                .from("schedule")
                .delete {
                    filter {
                        eq("itinerary_id", itineraryId)
                    }
                }

            val schedulesToInsert = schedules.map { schedule ->
                schedule.copy(
                    id = UUID.randomUUID().toString()
                )
            }

            val response = supabaseClient
                .client
                .postgrest
                .from("schedule")
                .insert(schedulesToInsert)

            return Resource.Success(true)
        } catch (e: Exception) {
            Timber.tag("SupabaseDatabaseError").e(e)
            Resource.Error(e.message)
        }
    }

    suspend fun getItinerary(
        itineraryId: String,
    ): Resource<TestingLlmResponse> {
        return try {
            val colums = Columns.raw(
                """
                *,
                schedule(
                    id,
                    itinerary_id,
                    trip_day,
                    date,
                    activity_name,
                    address,
                    place_name,
                    activity_category,
                    completed,
                    photo_url,
                    details,
                    time,
                    lat,
                    long,
                    order,
                    rating,
                    review
                )
            """.trimIndent()
            )
            val response = supabaseClient
                .client
                .postgrest
                .from("itinerary")
                .select(columns = colums) {
                    filter {
                        eq("itinerary_id", itineraryId)
                    }
                }
                .decodeSingle<TestingLlmResponse>()

            return Resource.Success(response)
        } catch (e: Exception) {
            Timber.tag("SupabaseDatabaseError").e(e)
            Resource.Error(e.message)
        }
    }

    @OptIn(SupabaseExperimental::class)
    fun getRealtimeSchedules(
        itineraryId: String,
    ): Flow<List<ScheduleItem>> =
        supabaseClient.client.from("schedule").selectAsFlow(
            ScheduleItem::id,
            filter = FilterOperation("itinerary_id", FilterOperator.ILIKE, itineraryId)
        )

    suspend fun insertReview(review: Review): Resource<Boolean> {
        return try {
            val reviewToInsert = review.copy(
                id = null,
                userId = supabaseAuth.getUserId().data
            )
            val response = supabaseClient
                .client
                .postgrest
                .from("review")
                .insert(reviewToInsert)

            return Resource.Success(true)
        } catch (e: Exception) {
            Timber.tag("SupabaseDatabaseError").e(e)
            Resource.Error(e.message)
        }
    }
}