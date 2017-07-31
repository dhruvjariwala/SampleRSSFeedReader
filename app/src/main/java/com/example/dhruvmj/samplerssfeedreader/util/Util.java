package com.example.dhruvmj.samplerssfeedreader.util;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;

/**
 * @author Dhruv Jariwala
 */

public class Util {
    public static <T extends Parcelable> T[] getParcelableArrayData(@Nullable Bundle bundle, String key,
                                                                    Parcelable.Creator<T> creator) {
        Parcelable[] parcelableData = bundle != null ? bundle.getParcelableArray(key) : null;
        T[] dataArray;
        if (parcelableData != null) {
            dataArray = creator.newArray(parcelableData.length);
            System.arraycopy(parcelableData, 0, dataArray, 0, parcelableData.length);
        } else {
            dataArray = creator.newArray(0);
        }
        return dataArray;
    }
}
