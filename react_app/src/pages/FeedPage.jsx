import { useContext, useEffect, useState } from "react";
import { UserContext } from "../contexts/UserContext";
import NewsFeed from "../components/NewsFeed";
import Header from "../components/Header";

function FeedPage({...props}) {
	const pageSize = 10
	const { profile } = useContext(UserContext)
	

	return ( 
		<div {...props}>
			<Header/>
			<NewsFeed 
				pageSize={pageSize}
				profile={profile}
				/>
		</div>
	);
}

export default FeedPage;