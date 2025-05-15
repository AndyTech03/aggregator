import { useContext } from "react";
import { UserContext } from "../contexts/UserContext";
import TopNewsList from "../components/TopNewsList";

function HomePage({ ...props }) {
	const user = useContext(UserContext)
	return ( 
		<div {...props}>
			{user.userId} <br />
			{user.profile}
			<TopNewsList />
		</div>
	);
}

export default HomePage;