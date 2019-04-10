==NodeCore MakeAddress==

This will create a new NodeCore wallet.
It runs completely offline, and without any need for a full NodeCore node.
This address can be used for mining.
If someone sends coins to this address, it will show up in the blockchain.

This wallet can later be imported into a full node with the importwallet command:
https://wiki.veriblock.org/index.php?title=NodeCore_CommandLine#importwallet

===USAGE==
CASE 1: Create an address
pass in no parameters, simply run the tool

CASE 2: Create a vanity address
Warning - this could take a long time to generate, depending on the complexity of the match
Pass in command parameters: <token> <mustStartWith> <ThreadCount>

Example: Create an address, searching on 4 threads, that starts with 'ABC', like VABC...
makeaddress ABC true 4

Example: Create an address, searching on 2 threads, containing the phrase 'ABC' anywhere, like VxxxxABC...
makeaddress ABC false 2






