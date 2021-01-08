// MakeAddress PoC
// Copyright 2017-2021 Xenios SEZC.
// All rights reserved.
// https://www.veriblock.org
// Distributed under the MIT software license, see the accompanying
// file LICENSE or http://www.opensource.org/licenses/mit-license.php.

package veriblock;

import java.time.Instant;

public class Utils {

    public static long getEpochCurrent()
    {
        Instant instant = Instant.now();
        long timeStampSeconds = instant.getEpochSecond();
        return timeStampSeconds;
    }
}
