package com.wruzjan.ihg.utils.threading;

import com.wruzjan.ihg.utils.dao.ProtocolDataSource;
import com.wruzjan.ihg.utils.model.Protocol;

import androidx.annotation.NonNull;

public class GetSiemanowiceByProtocolIdAsyncTask extends BaseAsyncTask<Integer, Protocol> {

    @NonNull private final ProtocolDataSource protocolDataSource;

    public GetSiemanowiceByProtocolIdAsyncTask(@NonNull ProtocolDataSource protocolDataSource) {
        this.protocolDataSource = protocolDataSource;
    }

    @Override
    protected Protocol doInBackground(Integer... integers) {
        int protocolId = integers[0];
        return protocolDataSource.getSiemianowiceProtocolsById(protocolId);
    }
}
