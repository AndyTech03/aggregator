import React, { useState } from "react";
import useValue from "../hooks/useValue";
import NewsFeed from "../components/NewsFeed";
import useArray from "../hooks/useArray";

function SearchPage({ ...props }) {
	const pageSize = 10;
	const [query, setQuery] = useValue({
		cookiesName: 'searchQuery',
		defaultValue: '',
		useStorage: true,
	})
	
	const categoriesFilter = useArray({
		cookiesName: 'categoriesFilter',
		defaultValue: [],
		useStorage: true,
		global: true,
	})
	const authorsFilter = useArray({
		cookiesName: 'authorsFilter',
		defaultValue: [],
		useStorage: true,
		global: true,
	})
	const rssFeedsFilter = useArray({
		cookiesName: 'rssFeedsFilter',
		defaultValue: [],
		useStorage: true,
		global: true,
	})

	return ( 
		<div>
			{authorsFilter?.items?.map((author, idx) => 
				<span key={'author' + idx + author}>"{author}"; </span>
			)} <br />
			{categoriesFilter?.items?.map((category, idx) => 
				<span key={'category' + idx + category}>"{category}"; </span>
			)} <br />
			{rssFeedsFilter?.items?.map((rssFeed, idx) => 
				<span key={'rssFeed' + idx + rssFeed}>"{rssFeed}"; </span>
			)} <br />
			<input type="search"
				placeholder="Поиск..."
				value={query} onChange={(e)=> setQuery(e.target.value)} 
				/>
			<NewsFeed
				query={query}
				pageSize={pageSize}
				/>
		</div>
	);
}

export default SearchPage;