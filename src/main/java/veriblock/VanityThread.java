// MakeAddress PoC
// Copyright 2017-2019 Xenios SEZC.
// All rights reserved.
// https://www.veriblock.org
// Distributed under the MIT software license, see the accompanying
// file LICENSE or http://www.opensource.org/licenses/mit-license.php.

package veriblock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.veriblock.core.utilities.AddressUtility;
import org.veriblock.core.wallet.AddressKeyGenerator;

import java.security.KeyPair;

public class VanityThread extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(VanityThread.class);

    //Simple thread approach - just keep going

    public VanityThread(VanityState vs, int threadNumber, boolean mustStartWith, String mustContainToken)
    {
        this.mustStartWith = mustStartWith;
        this.mustContainToken = mustContainToken;
        _threadNumber = threadNumber;
        _attempts = 0;
        _vanityState = vs;
    }

    private VanityState _vanityState;
    private int _threadNumber;
    public void run(){
        logger.info("Start Thread {}", _threadNumber);
        doBatch();
    }

    private boolean mustStartWith;
    private String mustContainToken;

    public long getAttempts()
    {
        return _attempts;
    }

    private long _attempts;

    private void doBatch() {
        int iBatchSize = 1000;
        boolean shouldContinue = true;

        long attempts = 0;

        while (shouldContinue) {
            try {
                if (_vanityState.shouldStopThread())
                {
                    return;
                }

                attempts++;

                KeyPair keyPair = AddressKeyGenerator.generate();
                String strAddress = AddressUtility.addressFromPublicKey(keyPair.getPublic());

                boolean foundMath = false;
                if (mustStartWith) {
                    if (strAddress.startsWith("V" + mustContainToken)) {
                        foundMath = true;
                    }
                } else {
                    if (strAddress.contains(mustContainToken)) {
                        foundMath = true;
                    }
                }

                if (foundMath) {
                    _attempts = attempts;
                    _vanityState.setResult(keyPair);
                    return;
                } else {
                    //Do some logging that we're still alive
                    if (attempts % iBatchSize == 0) {
                        _attempts = attempts;
                        logger.info("[{}] Attempt count: {}", _threadNumber, attempts);
                    }
                }
            } catch (Exception ex) {
                //Should never be hit
                ex.printStackTrace();
            }
        }
    }
}
