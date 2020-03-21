// MakeAddress PoC
// Copyright 2017-2020 Xenios SEZC.
// All rights reserved.
// https://www.veriblock.org
// Distributed under the MIT software license, see the accompanying
// file LICENSE or http://www.opensource.org/licenses/mit-license.php.

package veriblock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.veriblock.core.bitcoinj.Base58;
import org.veriblock.core.utilities.AddressUtility;
import org.veriblock.core.wallet.DefaultAddressManager;
import java.io.File;
import java.security.KeyPair;

public class Vanity
{
    private static final Logger logger = LoggerFactory.getLogger(Vanity.class);

    public Vanity(String mustContainToken, int iThreadCount, boolean mustStartWith)
    {
        _mustContainToken = mustContainToken;
        _mustStartWith = mustStartWith;
        _threadCount = iThreadCount;
    }

    private int _threadCount;
    private String _mustContainToken;
    private boolean _mustStartWith;

    public void createVanityAddress()
    {
        logger.info("About to attempt to create vanity address for token '{}'", _mustContainToken);

        //validate input
        boolean  isBase58 = Base58.isBase58String(_mustContainToken);
        if (!isBase58)
        {
            logger.info("'{}' is not a valid Base58 string. VBK Addresses must be Base58. No wallet created.", _mustContainToken);
            return;
        }

        logger.info("Run search on {} thread(s)", _threadCount);
        logger.info("Search is case-sensitive");
        logger.info("This may take some time...");
        if (_mustStartWith)
        {
            logger.info("Address must start with 'V{}' (VBK addresses always start with V first)", _mustContainToken);
        }
        else
        {
            logger.info("Address can contain '{}' anywhere", _mustContainToken);
        }

        //BIG RUN:

        VanityThread[] athread = new VanityThread[_threadCount];
        VanityState vanityState = new VanityState();
        for (int i = 0; i<_threadCount; i++)
        {
            athread[i] = new VanityThread(vanityState, i,  _mustStartWith, _mustContainToken);
            athread[i].start();
        }

        //wait for all threads to finish
        for (int i = 0; i<_threadCount; i++)
        {
            try {
                athread[i].join();
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }

        //Got result!
        long totalAttempts = 0;
        for (int i = 0; i < _threadCount; i++)
        {
            totalAttempts = totalAttempts + athread[i].getAttempts();
        }

        KeyPair keyPair = vanityState.getResult();
        String strAddressMatch = AddressUtility.addressFromPublicKey(keyPair.getPublic());
        String newPath = strAddressMatch + ".dat";

        //found the match!
        int position = strAddressMatch.indexOf(_mustContainToken);
        logger.info("Found address {} on attempt# {} that matches '{}' at position {}",
                strAddressMatch, totalAttempts, _mustContainToken, position);

        String finalPath = saveToFile(keyPair, newPath);

        logger.info("Wallet file: {}", finalPath);

    }

    private static String saveToFile( KeyPair keyPair, String walletPath) {
        DefaultAddressManager addressManager = null;
        String fullWalletPath = null;
        try {

            //get absolute path
            File f = new File(walletPath);
            fullWalletPath = f.getAbsolutePath();

            addressManager = new DefaultAddressManager();
            addressManager.importKeyPair(
                    keyPair.getPublic().getEncoded(),
                    keyPair.getPrivate().getEncoded());
            addressManager.saveWalletToFile(fullWalletPath);
        }
        catch (Exception ex)
        {
            logger.error("Error saving wallet file: {}, {}", fullWalletPath, ex);
        }
        return fullWalletPath;
    }


}
