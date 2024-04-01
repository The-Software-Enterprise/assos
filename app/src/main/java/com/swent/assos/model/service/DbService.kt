package com.swent.assos.model.service

import com.swent.assos.model.data.Association

interface DbService {
    suspend fun getAllAssociations(): List<Association>
}
