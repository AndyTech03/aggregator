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
	})
}

export default async function fetchArticle(itemId) {
	return items.find(i => i.id == itemId)
}