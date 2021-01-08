// MakeAddress PoC
// Copyright 2017-2021 Xenios SEZC.
// All rights reserved.
// https://www.veriblock.org
// Distributed under the MIT software license, see the accompanying
// file LICENSE or http://www.opensource.org/licenses/mit-license.php.

package veriblock;

import org.veriblock.core.contracts.*;
import org.veriblock.core.wallet.DefaultAddressManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        logger.info("About to create wallet file:");

        if (args.length != 3)
        {
            makeSimpleAddress();
        }
        else
        {

            try {
                String vanityString = args[0];
                Boolean mustStartWith = Boolean.parseBoolean(args[1]);
                int iThreadCount = Integer.parseInt(args[2]);

                Vanity v = new Vanity(vanityString, iThreadCount, mustStartWith);
                v.createVanityAddress();
            }
            catch (Exception ex)
            {
                logger.info("Usage: makeaddress [token] [mustStartWith] [ThreadCount]");
                logger.error("Could not run createVanityAddress. {}", ex);
            }
        }

        logger.info("Done");
        logger.info("");
    }

    private static void makeSimpleAddress()
    {
        long epoch = Utils.getEpochCurrent();
        String walletPath = "wallet_" + Long.toString(epoch) + ".dat";

        try {
            File wallet = new File(walletPath);

            AddressManager addressManager = new DefaultAddressManager();
            addressManager.load(wallet);

            String newAddress = addressManager.getDefaultAddress().getHash();

            //Rename file to match the wallet address
            String newPath = newAddress + ".dat";
            wallet.renameTo(new File(newPath));

            String result = String.format("Created wallet file %1$s with address %2$s",
                    newPath,
                    addressManager.getDefaultAddress().getHash());
            logger.info(result);
        }
        catch (Exception ex)
        {
            logger.error("There was an error trying to create the wallet file: {}", ex);
        }

    }

}
