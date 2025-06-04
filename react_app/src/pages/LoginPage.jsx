import { useContext, useState } from "react";
import { UserContext } from "../contexts/UserContext";
import Header from "../components/Header";

function LoginPage({logInUser, ...props}) {
	const user = useContext(UserContext)
	const [login, setLogin] = useState('')
	const [password, setPassword] = useState('')
	return ( 
		<div {...props}>
			<Header/>
			{user.userId} <br />
			{user.profile}
			<br />
			Login: <input value={login} onChange={(e) => setLogin(e.target.value)}/>
			Password: <input value={password} onChange={(e) => setPassword(e.target.value)}/>
			<button onClick={() => {
				logInUser(login, password)
			}}>Войти</button>
		</div>
	);
}

export default LoginPage;