// MakeAddress PoC
// Copyright 2017-2020 Xenios SEZC.
// All rights reserved.
// https://www.veriblock.org
// Distributed under the MIT software license, see the accompanying
// file LICENSE or http://www.opensource.org/licenses/mit-license.php.

package veriblock;

import java.security.KeyPair;
import java.util.concurrent.locks.ReentrantLock;

public class VanityState {

    public VanityState()
    {

    }

    public boolean shouldStopThread()
    {
        if (_result == null)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    private ReentrantLock lock = new ReentrantLock();

    private KeyPair _result = null;
    public KeyPair getResult()
    {
        return _result;
    }
    public void setResult(KeyPair result)
    {
        lock.lock();
        try {
            _result = result;
        } finally {
            lock.unlock();
        }
    }
}
