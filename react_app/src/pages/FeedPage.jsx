import React, { useEffect, useState } from "react";
import FeedItem from "../components/FeedItem";
import { useLocation } from "react-router-dom";
import useArray from "../hooks/useArray";

function FeedPage({ ...props }) {
	const location = useLocation();
	const categoriesFilter = useArray("categoriesFilter")
	const rssFeedsFilter = useArray("rssFeedsFilter")
	const authorsFilter = useArray("authorsFilter")
	const clearFilters = () => {
		categoriesFilter.setItems([])
		rssFeedsFilter.setItems([])
		authorsFilter.setItems([])
	}
	const feed = [
		{
			author: "Сетевое издание UG.RU",
			categories: [
				"Новости",
				"Образование UG.RU",
				"Учитель года",
				"Мурманская область",
			],
			title: "В Мурманской области объявили победителя конкурса «Учитель года – 2025»",
			description: `
				<p>
					18 апреля состоялось торжественное награждение победителей регионального этапа престижного конкурса «Учитель года – 2025» в стенах Центра опережающей профессиональной подготовки. 
					За звание лучших соревновались 23 преподавателя, представлявших 16 различных муниципальных образований региона, среди которых четыре участника выступали в специальной номинации «Дебют». 
					«На протяжении этих четырех дней мы увидели множество творческих идей и нестандартных подходов к...
				</p>
				<p>
					Сообщение 
					<a rel="nofollow" href="https://ug.ru/v-murmanskoj-oblasti-obyavili-pobeditelya-konkursa-uchitel-goda-2025/">
					В Мурманской области объявили победителя конкурса «Учитель года – 2025»
					</a> появились сначала на 
					<a rel="nofollow" href="https://ug.ru">
					Учительская газета</a>.
				</p>
			`,
			feedUrl: "http://www.ug.ru/rss",
			date: "2025-04-20 15:01:11",
			uri: "https://ug.ru/v-murmanskoj-oblasti-obyavili-pobeditelya-konkursa-uchitel-goda-2025/"
		},
		{
			author: "Сетевое издание UG.RU",
			categories: [
				"Новости",
				"Образование UG.RU",
				"Учитель года",
				"Мурманская область",
			],
			title: "В Мурманской области объявили победителя конкурса «Учитель года – 2025»",
			description: `
				<p>
					18 апреля состоялось торжественное награждение победителей регионального этапа престижного конкурса «Учитель года – 2025» в стенах Центра опережающей профессиональной подготовки. 
					За звание лучших соревновались 23 преподавателя, представлявших 16 различных муниципальных образований региона, среди которых четыре участника выступали в специальной номинации «Дебют». 
					«На протяжении этих четырех дней мы увидели множество творческих идей и нестандартных подходов к...
				</p>
				<p>
					Сообщение 
					<a rel="nofollow" href="https://ug.ru/v-murmanskoj-oblasti-obyavili-pobeditelya-konkursa-uchitel-goda-2025/">
					В Мурманской области объявили победителя конкурса «Учитель года – 2025»
					</a> появились сначала на 
					<a rel="nofollow" href="https://ug.ru">
					Учительская газета</a>.
				</p>
			`,
			feedUrl: "http://www.ug.ru/rss",
			date: "2025-04-20 15:01:11",
			uri: "https://ug.ru/v-murmanskoj-oblasti-obyavili-pobeditelya-konkursa-uchitel-goda-2025/"
		},
		{
			author: "Сетевое издание UG.RU",
			categories: [
				"Новости",
				"Образование UG.RU",
				"Учитель года",
				"Мурманская область",
			],
			title: "В Мурманской области объявили победителя конкурса «Учитель года – 2025»",
			description: `
				<p>
					18 апреля состоялось торжественное награждение победителей регионального этапа престижного конкурса «Учитель года – 2025» в стенах Центра опережающей профессиональной подготовки. 
					За звание лучших соревновались 23 преподавателя, представлявших 16 различных муниципальных образований региона, среди которых четыре участника выступали в специальной номинации «Дебют». 
					«На протяжении этих четырех дней мы увидели множество творческих идей и нестандартных подходов к...
				</p>
				<p>
					Сообщение 
					<a rel="nofollow" href="https://ug.ru/v-murmanskoj-oblasti-obyavili-pobeditelya-konkursa-uchitel-goda-2025/">
					В Мурманской области объявили победителя конкурса «Учитель года – 2025»
					</a> появились сначала на 
					<a rel="nofollow" href="https://ug.ru">
					Учительская газета</a>.
				</p>
			`,
			feedUrl: "http://www.ug.ru/rss",
			date: "2025-04-20 15:01:11",
			uri: "https://ug.ru/v-murmanskoj-oblasti-obyavili-pobeditelya-konkursa-uchitel-goda-2025/"
		},
	]
	if (feed.length < 0)
		return <div {...props}>Ничего не найдено...</div>
	return ( 
		<div {...props}>
			<div>{JSON.stringify(location.state)}</div>
			<div>{JSON.stringify(categoriesFilter.items)}</div>
			<div>{JSON.stringify(rssFeedsFilter.items)}</div>
			<div>{JSON.stringify(authorsFilter.items)}</div>
			<div><button onClick={() => clearFilters()}>Clear</button></div>
			{feed.map((item, key) => 
				<FeedItem filter={{
					categories: categoriesFilter,
					rssFeeds: rssFeedsFilter,
					authors: authorsFilter,
				}} item={item} key={key}/>
			)}
		</div>
	 );
}

export default FeedPage;