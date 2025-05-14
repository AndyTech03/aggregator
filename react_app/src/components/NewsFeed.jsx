import React, { useEffect, useRef, useState } from "react";
import FeedItem from "./FeedItem";
import RefreshWidget from "./RefreshWidget";
import useValue from "../hooks/useValue";
import { fetchLatest } from "../api/rssItemsController";

function NewsFeed({ title='Новости', pageSize: limit, query, profile, similarId, ...props }) {
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
	const [loading, setLoading] = useState(true)
	
	const getFeed = (loadOffset=null) => {
		setLoading(true)
		return fetchLatest(loadOffset == null ? offset : loadOffset, limit)
		.then(newItems => {
			console.log(newItems);
			setLoading(false)
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
			setFeedItems(newFeed)
			setOffset(limit)
		})
	}
	const next = () => {
		setOffset(prev => prev + limit)
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
		if (feedState?.items != null) {
			if (feedState.items.length == 0)
				init()
			else {
				setFeedItems(feedState.items)
				setLoading(false)
			}
		}
		if (feedState?.offset != null) {
			setOffset(feedState.offset)
		}
		if (feedState?.scrollY != null) {
			setScrollY(feedState.scrollY)
		}
	}, [feedState, limit, query, profile, similarId])

	return ( 
		<div {...props}>
			<b ref={titleRef}>{title}</b>
			<RefreshWidget reset={reset} goTop={goTop} goBottom={goBottom} />
			{feedItems.map((item, idx) =>
				<FeedItem item={item} key={item.id || idx} saveFeedState={save} />
			)}
					<hr />
			{loading ?
				<>
					<p>Loading...</p>
				</> :
				<>
					{hasMore && 
						<button 
							onClick={() => {setLoading(true); setTimeout(next, 1000)}}
						>
							Next
						</button>
					}
				</>
			}
			<div ref={bottomRef} />
		</div>
	);
}

export default NewsFeed;