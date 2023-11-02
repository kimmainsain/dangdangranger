import { useEffect, useRef, useState } from "react";
import {
	View,
	Text,
	Image,
	TouchableOpacity,
	ScrollView,
	TextInput,
} from "react-native";
// import GestureFlipView from "../components/GestureFlipView";
import CommonLayout from "../recycles/CommonLayout";
// import ProfileItem from "../components/ProfileItem";
// import NftProfile from "../components/NftProfile";

import WhiteHeader from "../recycles/WhiteHeader";
// import SubMain from "../components/SubMain";
// import SubMainImg from "../../assets/images/sub-main-bg.png";
// import rightArrowIcon from "../../assets/images/right-arrow.png";
import ProfileLayout from "../styles/profileLayout";
import TempImg from "../../assets/images/templogo.png";
import Footer from "../recycles/Footer";
import ColorHeader from "../recycles/ColorHeader";
import AlbumLayout from "../styles/albumLayout";
import TempProfileImg from "../../assets/images/dog1.jpg";
import WhitePenIcon from "../../assets/images/pen-icon.png";
import FourBtn from "../recycles/PetrolBtn";
import AbsoluteVar from "../recycles/FooterBar";
import MainLayout from "../styles/mainLayout";
import CustomButton from "../recycles/CustomBtn";
import NFTImg from "../../assets/images/NFTImg.png";

import WalletProcess from "../components/WalletProcess";
import WalletLoading from "../components/WalletLoading";

import CreateWalletPasswordLayout from "../styles/createWalletPasswordLayout";

const Profile = ({ navigation }: any) => {
	const flipView = useRef<any>();
	const [dogList, setDogList] = useState<any>([]);
	const [isLoading, setIsLoading] = useState<Boolean>(false);
	const [isChecked, setIsChecked] = useState<Boolean>(false);
	const [password, setPassword] = useState<string>("");
	const [checkPassword, setCheckPassword] = useState<string>("");

	const RPC_URL = process.env.RPC_URL;
	const SECRET_SALT = process.env.SECRET_SALT;
	const NFT_STORAGE = process.env.NFT_STORAGE;

	const createWallet = async () => {
		// if(!isChecked){
		//     alert('비밀번호 복구불가 안내 문구에 체크해주세요.');
		//     return;
		// }
		// if(password !== checkPassword){
		//     alert('비밀번호를 다시 확인해주세요.');
		//     return;
		// }
		// if(!isLoading){
		//     setIsLoading(true);
		// }
		// try{
		//     axiosApi.put('/user/wallet', {
		//         "userWalletPw": password,
		//     })
		// }catch(err){
		//     alert("지갑 생성 오류, 관리자에게 문의하세요.");
		//     console.error("비밀번호 저장 오류");
		// }
		// try {
		//     axios.get('https://idog.store/blockchain/wallet').then(async (data) => {
		//         const encryptedValue = data.data;
		//         const decrypted = await decryptValue(encryptedValue, SECRET_SALT);
		//         const newAccount = await ethers.HDNodeWallet.fromPhrase(decrypted);
		//         console.log("newAccount", newAccount);
		//         await SecureStore.setItemAsync("walletAddress", newAccount?.address);
		//         await SecureStore.setItemAsync("privateKey", newAccount?.privateKey);
		//         await SecureStore.setItemAsync("mnemonic", String(newAccount?.mnemonic?.phrase));
		//         const walletAddress = await SecureStore.getItemAsync("walletAddress");
		//         const privateKey = await SecureStore.getItemAsync("privateKey");
		//         const Mnemonic = await newAccount?.mnemonic?.phrase;
		//         try{
		//             const addressDbApi = await axiosApi.put('/user/address', {
		//                 "userAddress": walletAddress,
		//             });
		//             const walletApi = await axiosApi.put('/user/wallet',{
		//                 "userWalletPw": password,
		//             });
		//             if(addressDbApi.status === 200 && walletApi.status === 200){
		//                 setIsLoading(false);
		//                 setIsChecked(false);
		//                 await navigation.navigate('ProtectWallet');
		//             }else{
		//                 alert("지갑 생성 실패, 관리자에게 문의하세요.");
		//                 setIsLoading(false);
		//                 console.error("Error generating wallet");
		//             }
		//         }catch(err){
		//             alert("지갑 생성 실패, 관리자에게 문의하세요.");
		//             setIsLoading(false);
		//             console.error("Error generating wallet:", err);
		//         }
		//     }).catch((err) => {
		//         alert("지갑 생성 실패, 관리자에게 문의하세요.");
		//         setIsLoading(false);
		//         console.error("Error generating wallet:", err);
		//     })
		// } catch (error) {
		//     alert("지갑 생성 실패, 관리자에게 문의하세요.");
		//     setIsLoading(false);
		//     console.error("Error generating wallet:", error);
		// }
	};

	const decryptValue = (encrypted: any, secretkey: any) => {
		// const bytes = CryptoJS.AES.decrypt(encrypted, secretkey);
		// const originalText = bytes.toString(CryptoJS.enc.Utf8);
		// return originalText;
	};

	return (
		<>
			<CommonLayout>
				<ColorHeader title="지갑 설정" />
				<View style={CreateWalletPasswordLayout.titleWrap}>
					<Text style={CreateWalletPasswordLayout.mainTitle}>
						비밀번호 생성
					</Text>
					<Text style={CreateWalletPasswordLayout.subTitle}>
						이 비밀번호는 이 기기에서 귀하의 POPPY WALLET 지갑을 잠금 해제할
						때만 사용됩니다.
					</Text>
				</View>

				<WalletProcess />

				<View style={CreateWalletPasswordLayout.formWrap}>
					<Text style={CreateWalletPasswordLayout.formTitle}>
						신규 비밀번호
					</Text>
					<TextInput
						placeholder="신규 비밀번호를 입력해주세요."
						style={CreateWalletPasswordLayout.formInput}
						// value={password}
						// onChangeText={(text) => setPassword(text)}
						secureTextEntry={true}
					/>
					<Text style={CreateWalletPasswordLayout.formTitle}>
						비밀번호 확인
					</Text>
					<TextInput
						placeholder="비밀번호 확인을 위해 다시 입력해주세요."
						style={CreateWalletPasswordLayout.formInput}
						// value={checkPassword}
						// onChangeText={(text) => setCheckPassword(text)}
						secureTextEntry={true}
					/>
					<View style={CreateWalletPasswordLayout.checkWrap}>
						{/* <Checkbox
                            style={CreateWalletPasswordLayout.checkbox}
                            value={isChecked}
                            onValueChange={setIsChecked}
                            color={isChecked ? "#9D9D9D" : "#9D9D9D"}
                        /> */}
						<Text style={CreateWalletPasswordLayout.checkInfo}>
							POPPY WALLET은 비밀번호를 복구해드릴 수 없습니다. 이를 이해하고
							확인하였습니다.
						</Text>
					</View>
				</View>

				<View style={CreateWalletPasswordLayout.buttonWrap}>
					{/* <TouchableOpacity activeOpacity={0.7} onPress={createWallet}> */}
					<TouchableOpacity
						activeOpacity={0.7}
						onPress={() => navigation.navigate("MakeWallet3")}
					>
						<View style={CreateWalletPasswordLayout.newCreateButton}>
							<Text style={CreateWalletPasswordLayout.newCreateButtonText}>
								비밀번호 생성하기
							</Text>
						</View>
					</TouchableOpacity>
				</View>
				<Footer />
			</CommonLayout>
			{/* {
                isLoading ?
                <WalletLoading title="지갑 생성 중.. 잠시만 기다려주세요"/>
                :
                <></>
            } */}
		</>
	);

	// return (
	// 	<>
	// 		<CommonLayout>
	// 			<ColorHeader title="주소 관리" />
	// 			<Text>지갑만들어주세요1</Text>
	// 			<CustomButton
	// 				text="월렛3페이지 이동"
	// 				onPress={() => navigation.navigate("MakeWallet3")}
	// 			/>
	// 		</CommonLayout>
	// 		<AbsoluteVar />
	// 	</>
	// );
};

export default Profile;
