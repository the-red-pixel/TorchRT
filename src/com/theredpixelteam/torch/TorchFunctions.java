package com.theredpixelteam.torch;

import com.theredpixelteam.torch.adm.ADM;

/**
 * Torch function control collection.
 */
public final class TorchFunctions {
    private TorchFunctions()
    {
    }

    /**
     * @see ADM#enabled()
     * @return Result
     */
    public static boolean isADMEnabled()
    {
        return ADM.enabled();
    }

    /**
     * @see ADM#enableADM()
     */
    public static void enableADM()
    {
        ADM.enableADM();
    }

    /**
     * @see ADM#disableADM()
     */
    public static void disableADM()
    {
        ADM.disableADM();
    }
}
