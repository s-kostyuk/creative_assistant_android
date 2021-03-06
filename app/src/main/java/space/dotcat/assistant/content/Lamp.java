package space.dotcat.assistant.content;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "Lamps", inheritSuperIndices = true)
public class Lamp extends Thing {

    @SerializedName("state")
    @ColumnInfo(name = "thing_state")
    private String mState;

    @SerializedName("is_powered_on")
    @ColumnInfo(name = "is_powered_on")
    private Boolean mIsPoweredOn;

    @SerializedName("is_active")
    @ColumnInfo(name = "thing_is_active")
    private Boolean mIsActive;

    @Ignore
    public Lamp() {
    }

    public Lamp(@NonNull String id, @NonNull List<String> capabilities, @NonNull List<String> commands,
                @NonNull Boolean isAvailable, @NonNull Boolean isEnabled, @NonNull String placement,
                @NonNull String type, @NonNull String friendlyName, @NonNull String state, @NonNull Boolean isPoweredOn,
                @NonNull Boolean isActive) {
        super(id, capabilities, commands, isAvailable, isEnabled, placement, type, friendlyName);

        mState = state;

        mIsPoweredOn = isPoweredOn;

        mIsActive = isActive;
    }

    public String getState() {
        return mState;
    }

    public void setState(String state) {
        mState = state;
    }

    public Boolean getIsActive() {
        return mIsActive;
    }

    public void setActive(Boolean active) {
        mIsActive = active;
    }

    public Boolean getIsPoweredOn() {
        return mIsPoweredOn;
    }

    public void setPoweredOn(Boolean poweredOn) {
        mIsPoweredOn = poweredOn;
    }
}
