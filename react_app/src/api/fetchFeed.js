const items = []
for (let i = 0; i < 100; i++) {
	items.push({
		id: i.toString(),
		author: "Сетевое издание UG.RU",
		categories: [
			"Новости",
			"Образование UG.RU",
			"Учитель года",
			"Мурманская область",
		],
		title: "В Мурманской области объявили победителя конкурса «Учитель года – 2025»",
		feedUrl: "http://www.ug.ru/rss",
		date: "2025-04-20 15:01:11",
		uri: "https://ug.ru/v-murmanskoj-oblasti-obyavili-pobeditelya-konkursa-uchitel-goda-2025/"
	})
}

export default async function fetchFeed(offset, count, profile) {
	console.log({offset, count})
	return items.slice(offset, offset+count);
}
