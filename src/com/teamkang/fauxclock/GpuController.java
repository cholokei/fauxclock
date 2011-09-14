
package com.teamkang.fauxclock;

import ru.org.amip.MarketAccess.utils.ShellInterface;

import android.content.Context;
import android.content.SharedPreferences;

public class GpuController {

    // gpu stuff
    private String gpuGoverner;
    private String gpuGovernerPath = "/sys/devices/platform/kgsl/msm_kgsl/kgsl-3d0/scaling_governor";
    private String gpuIOFraction;
    private String gpuIOFractionPath = "/sys/devices/platform/kgsl/msm_kgsl/kgsl-3d0/io_fraction";

    String[] govs = {
            "ondemand", "performance"
    };

    public SharedPreferences settings;
    public SharedPreferences.Editor editor;

    Context mContext;

    public GpuController(Context c) {
        mContext = c;

        settings = mContext.getSharedPreferences("gpu", 0);
        editor = settings.edit();
    }

    public void readGpuSettings() {
        // read gpu gov
        if (ShellInterface.isSuAvailable()) {
            gpuGoverner = ShellInterface.getProcessOutput("cat " + gpuGovernerPath);
            gpuIOFraction = ShellInterface.getProcessOutput("cat "
                    + gpuIOFractionPath);

        }
    }

    public String getCurrentActiveGov() {
        String g = "";

        if (ShellInterface.isSuAvailable()) {
            g = ShellInterface.getProcessOutput("cat " + gpuGovernerPath);
        }

        return g;
    }

    public void setGpuGoverner(String newGov) {
        if (ShellInterface.isSuAvailable()) {
            ShellInterface.runCommand("echo \"" + newGov + "\" > " + gpuGovernerPath);
            editor.putString("gpu_gov", newGov);
            gpuGoverner = newGov;
        }

    }

    public void setGpuIOFraction(int newFrac) {
        if (ShellInterface.isSuAvailable()) {
            ShellInterface.runCommand("echo \"" + newFrac + "\" > " + gpuIOFractionPath);
            editor.putInt("gpu_io_fraction", newFrac);
            gpuIOFraction = newFrac + "";
        }
    }

}
