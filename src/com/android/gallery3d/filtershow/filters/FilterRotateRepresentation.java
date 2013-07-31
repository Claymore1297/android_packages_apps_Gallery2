/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.gallery3d.filtershow.filters;

import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;

import com.android.gallery3d.R;
import com.android.gallery3d.filtershow.editors.EditorRotate;

import java.io.IOException;

public class FilterRotateRepresentation extends FilterRepresentation {
    public static final String SERIALIZATION_NAME = "ROTATION";
    public static final String SERIALIZATION_ROTATE_VALUE = "value";
    private static final String TAG = FilterRotateRepresentation.class.getSimpleName();

    Rotation mRotation = Rotation.ZERO;

    public enum Rotation {
        ZERO(0), NINETY(90), ONE_EIGHTY(180), TWO_SEVENTY(270);
        private final int mValue;

        private Rotation(int value) {
            mValue = value;
        }

        public int value() {
            return mValue;
        }

        public static Rotation fromValue(int value) {
            switch (value) {
                case 0:
                    return ZERO;
                case 90:
                    return NINETY;
                case 180:
                    return ONE_EIGHTY;
                case 270:
                    return TWO_SEVENTY;
                default:
                    return null;
            }
        }
    }

    public FilterRotateRepresentation(Rotation rotation) {
        super(FilterRotateRepresentation.class.getSimpleName());
        setSerializationName(SERIALIZATION_NAME);
        setShowParameterValue(true);
        setFilterClass(FilterRotateRepresentation.class);
        setFilterType(FilterRepresentation.TYPE_GEOMETRY);
        setTextId(R.string.rotate);
        setEditorId(EditorRotate.ID);
        setRotation(rotation);
    }

    public FilterRotateRepresentation(FilterRotateRepresentation r) {
        this(r.getRotation());
    }

    public FilterRotateRepresentation() {
        this(Rotation.ZERO);
    }

    public Rotation getRotation() {
        return mRotation;
    }

    public void set(FilterRotateRepresentation r) {
        mRotation = r.mRotation;
    }

    public void setRotation(Rotation rotation) {
        if (rotation == null) {
            throw new IllegalArgumentException("Argument to setRotation is null");
        }
        mRotation = rotation;
    }

    @Override
    public boolean allowsSingleInstanceOnly() {
        return true;
    }

    @Override
    public FilterRepresentation copy() {
        return new FilterRotateRepresentation(this);
    }

    @Override
    protected void copyAllParameters(FilterRepresentation representation) {
        if (!(representation instanceof FilterRotateRepresentation)) {
            throw new IllegalArgumentException("calling copyAllParameters with incompatible types!");
        }
        super.copyAllParameters(representation);
        representation.useParametersFrom(this);
    }

    @Override
    public void useParametersFrom(FilterRepresentation a) {
        if (!(a instanceof FilterRotateRepresentation)) {
            throw new IllegalArgumentException("calling useParametersFrom with incompatible types!");
        }
        setRotation(((FilterRotateRepresentation) a).getRotation());
    }

    @Override
    public boolean isNil() {
        return mRotation == Rotation.ZERO;
    }

    @Override
    public void serializeRepresentation(JsonWriter writer) throws IOException {
        writer.beginObject();
        writer.name(SERIALIZATION_ROTATE_VALUE).value(mRotation.value());
        writer.endObject();
    }

    @Override
    public void deSerializeRepresentation(JsonReader reader) throws IOException {
        boolean unset = true;
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (SERIALIZATION_ROTATE_VALUE.equals(name)) {
                Rotation r = Rotation.fromValue(reader.nextInt());
                if (r != null) {
                    setRotation(r);
                    unset = false;
                }
            } else {
                reader.skipValue();
            }
        }
        if (unset) {
            Log.w(TAG, "WARNING: bad value when deserializing " + SERIALIZATION_NAME);
        }
        reader.endObject();
    }
}