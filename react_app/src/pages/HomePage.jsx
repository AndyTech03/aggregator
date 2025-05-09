import { useContext } from "react";
import { UserContext } from "../contexts/UserContext";

function HomePage({ ...props }) {
	const user = useContext(UserContext)
	return ( 
		<div {...props}>
			{user.userId} <br />
			{user.profile}
		</div>
	);
}

export default HomePage;