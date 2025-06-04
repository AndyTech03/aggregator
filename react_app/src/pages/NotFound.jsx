import Header from "../components/Header";

function NotFoundPage({ ...props }) {
	return (
		<div {...props}>
			<Header/>
			<h2>404</h2>
			<h3>Страница не найдена!</h3>
		</div>
	);
}

export default NotFoundPage;