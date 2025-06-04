import React, { useState } from "react";
import useValue from "../hooks/useValue";
import NewsFeed from "../components/NewsFeed";
import useArray from "../hooks/useArray";
import TopCategoriesSelector from "../components/TopCategoriesSelector";
import Header from "../components/Header";

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
			<Header/>
			{authorsFilter?.items?.map((author, idx) => 
				<span key={'author' + idx + author}>"{author}"; </span>
			)} <br />
			{categoriesFilter?.items?.map((category, idx) => 
				<span key={'category' + idx + category}>"{category}"; </span>
			)} <br />
			{rssFeedsFilter?.items?.map((rssFeed, idx) => 
				<span key={'rssFeed' + idx + rssFeed}>"{rssFeed}"; </span>
			)} <br />
			<TopCategoriesSelector categoriesFilter={categoriesFilter} />
			<input type="search"
				placeholder="Поиск..."
				value={query} onChange={(e)=> setQuery(e.target.value)} 
				/>
			<NewsFeed
				query={query}
				categoriesFilter={categoriesFilter}
				pageSize={pageSize}
				/>
		</div>
	);
}

export default SearchPage;