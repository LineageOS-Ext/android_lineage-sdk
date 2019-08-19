/*
 * Copyright (c) 2019, The LineageOS Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package lineageos.health.records;

import android.os.Parcel;

import java.util.Objects;

import lineageos.health.Record;

/**
 * Abstract class that defines a {@link lineageos.health.Record} that holds
 * a single integer value
 */
public abstract class SimpleIntRecord extends Record {
    protected int value;

    public SimpleIntRecord(long time, int value) {
        super(time);
        this.value = value;
    }

    public SimpleIntRecord(long uid, long time, int value) {
        super(uid, time);
        this.value = value;
    }

    public SimpleIntRecord(long uid, long time, long duration, int value) {
        super(uid, time, duration);
        this.value = value;
    }

    /** @hide */
    public SimpleIntRecord(byte[] bytes) {
        super(bytes);
    }

    @Override
    protected void writeValueTo(Parcel dest) {
        dest.writeInt(value);
    }

    @Override
    protected void readValueFrom(Parcel parcel) {
        value = parcel.readInt();
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SimpleFloatRecord)) return false;
        if (!super.equals(o)) return false;
        SimpleFloatRecord that = (SimpleFloatRecord) o;
        return that.value == value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), value);
    }

    @Override
    public String toString() {
        return String.format(
            "Record { uid: %1$d, time: %2$d; duration: %3$d; value: %4$d }",
            uid, time, duration, value
        );
    }
}
