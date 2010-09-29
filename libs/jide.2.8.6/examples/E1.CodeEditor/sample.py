# file: blowfishECB.py

# Python Blowfish ECB Encryption to match published test vectors
# at http://www.schneier.com/code/vectors.txt

import chilkat

# The key and unencrypted data (clearBytes) are passed as hexidecimalized strings.
def blowfishECB( crypt, key, clearBytes, expectedAnswer ):
	
	# Set the secret key
	crypt.SetEncodedKey(key, 'hex')
	
	# Get the unencrypted data as binary data.
	unencryptedData = chilkat.CkByteData()
	crypt.Decode(clearBytes, 'hex', unencryptedData)
	
	encryptedHexString = chilkat.CkString()
	crypt.EncryptBytesENC(unencryptedData,encryptedHexString)
	
	# Chilkat padded to a multiple of 16 bytes, so we discard the padding.
	# (In this case, Chilkat added 8 NULL bytes to the original 8 bytes of data,
	# so we drop 16 characters - the hex encoding uses 2 characters per byte.)
	encryptedHexString.shorten(16)
	
	# Return a string that will be added to our output for visual verification.
	return encryptedHexString.getString() + ' should equal ' + expectedAnswer

crypt = chilkat.CkCrypt2()
crypt.UnlockComponent('anything for 30-day trial')
    
# Set the CryptAlgorithm to "blowfish2".  Chilkat's original implementation
# of Blowfish ("blowfish") returned encrypted data
# 4321 byte-swapped.  For backward compatibility, an additional CryptAlgorithm
# keyword ("blowfish2") was added to indicate the new Blowfish implementation.
crypt.put_CryptAlgorithm("blowfish2")
crypt.put_CipherMode("ecb")
    
# The test vectors use 64-bit encryption.  It is advised to use 128-bit encryption
# or higher for secure encryption.
crypt.put_KeyLength(64)
    
# Padding Schemes:
# 0 = (RFC2630) Each padding byte is the pad count (16 extra added if size is already a multiple of 16)
# 1 = Random bytes except the last is the pad count (16 extra added if size is already multiple of 16)
# 2 = Pad with random data. (If already a multiple of 16, no padding is added).
# 3 = Pad with NULLs. (If already a multiple of 16, no padding is added).
# 4 = Pad with SPACE chars(0x20). (If already a multiple of 16, no padding is added).
    
# To match the test vector output, we need to pad with NULL bytes.
crypt.put_PaddingScheme(3)

crypt.put_EncodingMode("hex")

print blowfishECB(crypt, "0000000000000000", "0000000000000000", "4EF997456198DD78")
print blowfishECB(crypt, "FFFFFFFFFFFFFFFF", "FFFFFFFFFFFFFFFF", "51866FD5B85ECB8A")
print blowfishECB(crypt, "3000000000000000", "1000000000000001", "7D856F9A613063F2")
print blowfishECB(crypt, "1111111111111111", "1111111111111111", "2466DD878B963C9D")
print blowfishECB(crypt, "0123456789ABCDEF", "1111111111111111", "61F9C3802281B096")
print blowfishECB(crypt, "1111111111111111", "0123456789ABCDEF", "7D0CC630AFDA1EC7")
print blowfishECB(crypt, "0000000000000000", "0000000000000000", "4EF997456198DD78")
print blowfishECB(crypt, "FEDCBA9876543210", "0123456789ABCDEF", "0ACEAB0FC6A0A28D")
print blowfishECB(crypt, "7CA110454A1A6E57", "01A1D6D039776742", "59C68245EB05282B")
print blowfishECB(crypt, "0131D9619DC1376E", "5CD54CA83DEF57DA", "B1B8CC0B250F09A0")
print blowfishECB(crypt, "07A1133E4A0B2686", "0248D43806F67172", "1730E5778BEA1DA4")
print blowfishECB(crypt, "3849674C2602319E", "51454B582DDF440A", "A25E7856CF2651EB")
print blowfishECB(crypt, "04B915BA43FEB5B6", "42FD443059577FA2", "353882B109CE8F1A")
print blowfishECB(crypt, "0113B970FD34F2CE", "059B5E0851CF143A", "48F4D0884C379918")
print blowfishECB(crypt, "0170F175468FB5E6", "0756D8E0774761D2", "432193B78951FC98")
print blowfishECB(crypt, "43297FAD38E373FE", "762514B829BF486A", "13F04154D69D1AE5")
print blowfishECB(crypt, "07A7137045DA2A16", "3BDD119049372802", "2EEDDA93FFD39C79")
print blowfishECB(crypt, "04689104C2FD3B2F", "26955F6835AF609A", "D887E0393C2DA6E3")
print blowfishECB(crypt, "37D06BB516CB7546", "164D5E404F275232", "5F99D04F5B163969")
print blowfishECB(crypt, "1F08260D1AC2465E", "6B056E18759F5CCA", "4A057A3B24D3977B")
print blowfishECB(crypt, "584023641ABA6176", "004BD6EF09176062", "452031C1E4FADA8E")
print blowfishECB(crypt, "025816164629B007", "480D39006EE762F2", "7555AE39F59B87BD")
print blowfishECB(crypt, "49793EBC79B3258F", "437540C8698F3CFA", "53C55F9CB49FC019")
print blowfishECB(crypt, "4FB05E1515AB73A7", "072D43A077075292", "7A8E7BFA937E89A3")
print blowfishECB(crypt, "49E95D6D4CA229BF", "02FE55778117F12A", "CF9C5D7A4986ADB5")
print blowfishECB(crypt, "018310DC409B26D6", "1D9D5C5018F728C2", "D1ABB290658BC778")
print blowfishECB(crypt, "1C587F1C13924FEF", "305532286D6F295A", "55CB3774D13EF201")
print blowfishECB(crypt, "0101010101010101", "0123456789ABCDEF", "FA34EC4847B268B2")
print blowfishECB(crypt, "1F1F1F1F0E0E0E0E", "0123456789ABCDEF", "A790795108EA3CAE")
print blowfishECB(crypt, "E0FEE0FEF1FEF1FE", "0123456789ABCDEF", "C39E072D9FAC631D")
print blowfishECB(crypt, "0000000000000000", "FFFFFFFFFFFFFFFF", "014933E0CDAFF6E4")
print blowfishECB(crypt, "FFFFFFFFFFFFFFFF", "0000000000000000", "F21E9A77B71C49BC")
print blowfishECB(crypt, "0123456789ABCDEF", "0000000000000000", "245946885754369A")
print blowfishECB(crypt, "FEDCBA9876543210", "FFFFFFFFFFFFFFFF", "6B5C5A9C5D9E0A5A")

