import { useContext, useEffect, useState } from "react";
import { UserContext } from "../contexts/UserContext";
import fetchFeed from "../api/fetchFeed";
import FeedItem from "../components/FeedItem";
import useValue from "../hooks/useValue";
import RefreshWidget from "../components/RefreshWidget";

function FeedPage() {
	const { profile } = useContext(UserContext)
	const [feedItems, setFeedItems]   = useState([]);      	// массив новостей
	const [offset,    setOffset]      = useState(0);    	// курсор/offset для keyset
	const [hasMore,   setHasMore]     = useState(true);    	// есть ли ещё
	const [scrollY, setScrollY]       = useState(null);
	const [feedState, setFeedState, saveFeedState] = useValue(
		'feedState', {offset: 0, scrollY: 0, items: []}, true)
	
	useEffect(() => {
		if (scrollY == null)
			return
		window.scrollTo(0, scrollY);
	}, [scrollY])

	useEffect(() => {
		console.log({feedState});
		if (feedState?.items && feedState.items.length != 0) {
			setFeedItems(feedState.items)
		}
		if (feedState?.offset) {
			setOffset(feedState.offset)
		}
		if (feedState?.scrollY) {
			setScrollY(feedState.scrollY)
		}
	}, [feedState]);

	const refreshFeed = () => {
		setFeedState(null)
		setFeedItems([])
		loadFeed()
	}

	const saveCurrentFeedState = () => {
		saveFeedState({
			offset: offset, 
			scrollY: window.scrollY,
			items: feedItems,
		})
	}
	const addItems = (newItems) => {
		setFeedItems(prev => {
			newItems = newItems.filter(item => 
				prev.find(copy => copy.id == item.id) == null
			)
			return [...prev, ...newItems]
		})
	}
	const pageSize = 1

	const loadFeed = (loadOffset=null, loadProfile=null) => {
		fetchFeed(offset, loadOffset || pageSize, loadProfile || profile)
		.then(newItems => {
			if (newItems.length == 0) {
				setHasMore(false)
				return;
			}
			addItems(newItems)
		})
	}

	useEffect(() => {
		loadFeed()
	}, [offset, profile])

	return ( 
		<div>
			<RefreshWidget refresh={refreshFeed} />
			{feedItems.map((item, idx) =>
				<FeedItem item={item} key={item.id || idx} saveFeedState={saveCurrentFeedState} />
			)}
			{hasMore && 
				<button onClick={() => setOffset(prev => prev + pageSize)}>Next</button>
			}
		</div>
	);
}

export default FeedPage;