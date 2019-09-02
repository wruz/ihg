package com.wruzjan.ihg.utils.threading;

import com.wruzjan.ihg.utils.dao.ProtocolNewPaderewskiegoDataSource;
import com.wruzjan.ihg.utils.model.ProtocolNewPaderewskiego;

import androidx.annotation.NonNull;

public class GetNewPaderewskiegoProtocolsByIdAsyncTask extends BaseAsyncTask<Integer, ProtocolNewPaderewskiego> {

    @NonNull private final ProtocolNewPaderewskiegoDataSource protocolNewPaderewskiegoDataSource;

    public GetNewPaderewskiegoProtocolsByIdAsyncTask(@NonNull ProtocolNewPaderewskiegoDataSource protocolNewPaderewskiegoDataSource) {
        this.protocolNewPaderewskiegoDataSource = protocolNewPaderewskiegoDataSource;
    }

    @Override
    protected ProtocolNewPaderewskiego doInBackground(Integer... integers) {
        int protocolId = integers[0];
        return protocolNewPaderewskiegoDataSource.getNewPaderewskiegoProtocolsById(protocolId);
    }
}
