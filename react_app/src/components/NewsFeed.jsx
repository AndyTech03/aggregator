import React, { useEffect, useRef, useState } from "react";
import FeedItem from "./FeedItem";
import RefreshWidget from "./RefreshWidget";
import useValue from "../hooks/useValue";
import fetchFeed from "../api/fetchFeed";

function NewsFeed({ title='Новости', pageSize, query, profile, similar, ...props }) {
	const titleRef = useRef(null)
	const bottomRef = useRef(null)
	const [feedItems, setFeedItems] = useState([]);
	const [offset,    setOffset] = useState(0);
	const [hasMore,   setHasMore] = useState(true);
	const [scrollY, setScrollY] = useState(null);
	const [feedState, setFeedState, saveFeedState] = useValue({
		cookiesName: 'feedState', 
		defaultValue: {offset: 0, scrollY: 0, items: []}, 
		useStorage: true,
	})
	
	const getFeed = (loadOffset=null) => {
		return fetchFeed(loadOffset == null ? offset : loadOffset, pageSize)
		.then(newItems => {
			if (newItems.length == 0) {
				setHasMore(false)
				return [];
			}
			return newItems
		})
	}
	const init = () => {
		scrollTo(0)
		getFeed(0)
		.then((newFeed) => {
			console.log('init', newFeed);
			setFeedItems(newFeed)
			setOffset(pageSize)
		})
	}
	const next = () => {
		setOffset(prev => prev + pageSize)
		append()
	}
	const scrollTo = () => {
		window.scrollTo(0, scrollY);
	}
	const goTop = () => {
		titleRef.current.scrollIntoView()
	}
	const goBottom = () => {
		bottomRef.current.scrollIntoView()
	}
	const save = () => {
		saveFeedState({
			offset: offset, 
			scrollY: window.scrollY,
			items: feedItems.slice(-20),
		})
	}
	const reset = () => {
		setFeedState(null)
		init()
		goTop()
	}
	const append = () => {
		getFeed()
		.then((newItems) => {
			setFeedItems(prev => {
				newItems = newItems.filter(item => 
					prev.find(copy => copy.id == item.id) == null
				)
				return [...prev, ...newItems]
			})
		})
	}

	useEffect(() => {
		if (scrollY == null)
			return
		scrollTo()
	}, [scrollY])

	useEffect(() => {
		console.log('feedState', feedState);
		if (feedState?.items != null) {
			console.log('set items');
			if (feedState.items.length == 0)
				init()
			else {
				setFeedItems(feedState.items)
			}
		}
		if (feedState?.offset != null) {
			console.log('set offset');
			setOffset(feedState.offset)
		}
		if (feedState?.scrollY != null) {
			console.log('set scrollY');
			setScrollY(feedState.scrollY)
		}
	}, [feedState, pageSize, query, profile, similar])

	return ( 
		<div {...props}>
			<b ref={titleRef}>{title}</b>
			<RefreshWidget reset={reset} goTop={goTop} goBottom={goBottom} />
			{feedItems.map((item, idx) =>
				<FeedItem item={item} key={item.id || idx} saveFeedState={save} />
			)}
			{hasMore && 
				<button onClick={next}>Next</button>
			}
			<div ref={bottomRef} />
		</div>
	);
}

export default NewsFeed;