<%@ page session="false"%>
<html>
<head>
<title>Upload File Request Page</title>
</head>
<body>

	<form method="POST"
		action="" enctype="multipart/form-data">
		File to upload: <input type="file" name="file"><br /> 
		testId<br> <input type="text" name="testId" value="1">
		<input type="submit" value="Upload">
	</form>
</body>
</html>