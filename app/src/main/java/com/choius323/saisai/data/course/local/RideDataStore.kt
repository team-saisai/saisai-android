package com.choius323.saisai.data.course.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.choius323.saisai.RecentRideProto
import kotlinx.coroutines.flow.Flow
import java.io.InputStream
import java.io.OutputStream

// Proto 객체를 읽고 쓰는 방법을 정의하는 Serializer
object RecentRideSerializer : Serializer<RecentRideProto> {
    override val defaultValue: RecentRideProto = RecentRideProto.getDefaultInstance()
    override suspend fun readFrom(input: InputStream): RecentRideProto =
        RecentRideProto.parseFrom(input)

    override suspend fun writeTo(t: RecentRideProto, output: OutputStream) =
        t.writeTo(output)
}

// Proto DataStore 인스턴스 생성
private val Context.protoDataStore: DataStore<RecentRideProto> by dataStore(
    fileName = "ride_prefs.pb",
    serializer = RecentRideSerializer
)

class RideDataStore(private val context: Context) {
    val recentRideProto: Flow<RecentRideProto> = context.protoDataStore.data

    suspend fun saveRecentRideProto(ride: RecentRideProto) {
        context.protoDataStore.updateData { ride }
    }
}