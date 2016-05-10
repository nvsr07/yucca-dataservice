<html>
	<head>
		<link rel="shortcut icon" href="favicon.png">
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<title>Smart Data Platform - Metadata REST API </title>
		<style>
			body{font-family: arial, sans-serif; letter-spacing: .01em; background-color: #f7f7f7; font-size: 11pt; line-height: 1.5em;margin: 0;}
			h1{clear: both; overflow: hidden; }
			h1 .logo{float: right;}
			a{color: #009640;}
			.lang-menu{ position: absolute; top:12px; right: 10%;float: right; font-size: 12px; font-weight: normal; }
			.lang-menu a, .lang-menu strong{margin-left:1em }
			h2{margin-top: 2em; font-size: 32px;}
			.indent{padding-left: 24px;}
			.content{margin: 64px  auto; width: 80%; }
			pre{display: block; padding: 1em; background-color: white; border: solid 1px #ebebeb; }
			table{display: block;  margin-top: 1em;border-collapse: collapse;font-size: 11pt; }
			td, th{ border: solid 1px #ebebeb; padding: .7em;}
			td{background-color: white; }
			th{background-color: #ebebeb; text-align: left; }
			
			.main-menu a{display: inline-block;font-size: 14pt; padding: 1em;  text-decoration: none;}
			.main-menu a:hover{ text-decoration: none; background-color: #009640; color: white;}
			.main-menu{border: solid black; border-width: 1px 0; padding: 0;}
			
			.url-dynamic-parameter-tenant{color:  #60a917; font-weight: bold;}
			.url-dynamic-parameter-smartobject{color:  #0050ef;font-weight: bold;}
			.url-dynamic-parameter-stream{color:  #d80073;font-weight: bold;}
			.url-dynamic-parameter-dataset{color:  #ba00ff;font-weight: bold;}
			.url-dynamic-parameter-package-id{color:  #ff9900;font-weight: bold;}
			ol li {padding: .7em;}
			
			footer{background-color: black; color: white; padding: 24px; text-align: center;}
			footer  a{color: white; text-decoration: none; padding: 0 48px;}

		</style>
	</head>
	<body>
		<div class='content'>

			<h1>
				Smartdatanet Metadata API
				<div class='logo'>
					<img src='https://userportal.smartdatanet.it/userportal/img/familyidentity/logosdp-top-navbar.png' alt='Smartdatanet'/>
				</div>
				<div class='lang-menu'>
					<a href='index.jsp'>Italiano</a>
					<strong>English</strong>
				</div>

			</h1>
			<p>Search and Detail API  REST for streams and datasets metadata hosted on  the SmartData Platform<p>
			<p class='main-menu'><a href="#search">Search</a><a href="#detail">Detail</a><a href="#auth">Authentication for private stream/dataset</a> </p>
			<h2 id="search">Search</h2>
			<div class='indent'>
				<p>
					<strong>Base Search URL with output in JSON format</strong><pre><code>https://api.smartdatanet.it/metadataapi/api/search/full?</code></pre>
				</p>
				<p>
					<strong>Base Search URL with output in CKAN format</strong><pre><code>https://api.smartdatanet.it/metadataapi/api/ckan/2/package_list?</code></pre>
				</p>
				<strong>Parameters</strong> 
				<table>
					<thead><tr><th>Key</th><th>Desc</th><th>Possible values</th></tr></thead>
					<tbody>
						<tr><td>q</td><td>search query</td><td>Free text</td></tr>
						<tr><td>tenant</td><td>Tenant code of the owner of the stream/dataset</td><td>If used a unkonwn tenant code returns zero values</td></tr>
						<tr><td>domain</td><td>Domain code of the stream/dataset (Agriculture, Energy &hellip;)</td><td>If used a unkonwn domain code returns zero values </td></tr>
						<tr><td>opendata</td><td>Properties of the dataset/stream  to be opendata</td><td>If used opendata=true will be extract only opendata, if used any other value will be extract only NOT opendata. If omitted extract opendata and not opendata</td></tr>
						<tr><td>start</td><td>Pagination: first  row that will be extract</td><td>Numeric (if omitted, it will used zero)</td></tr>
						<tr><td>end</td><td>Pagination: number of row that will be extract</td><td>Numeric (if omitted, it will used 12). Max value: 1000</td></tr>
						<tr><td>lang</td><td>Lang for the domain and tags of the dataset/stream</td><td>Language managed: italian (lang=it), english(lang=en)</td></tr>
					</tbody>
				</table>
			</div>
			<h2 id="detail">Detail</h2>
			<div class='indent'>
				<p>
					<strong>Base Detail URL for stream with output in JSON format</strong><pre><code>https://api.smartdatanet.it/metadataapi/api/detail/<span class='url-dynamic-parameter-tenant'>{{tenantCode}}</span>/<span class='url-dynamic-parameter-smartobject'>{{smartobjectCode}}</span>/<span class='url-dynamic-parameter-stream'>{{streamCode}}</span>?</code></pre>
				where: 
					<ul>
						<li><span class='url-dynamic-parameter-tenant'>{{tenantCode}}</span> is the Tenant code of the owner of the stream</li>
						<li><span class='url-dynamic-parameter-smartobject'>{{smartobjectCode}}</span> is the  Smart Object code used by  the stream</li>
						<li><span class='url-dynamic-parameter-stream'>{{streamCode}}</span>is the  Stream code</li>
					</ul>
				</p>
				<p>

					<strong>Base Detail URL for dataset with output in JSON format </strong><pre><code>https://api.smartdatanet.it/metadataapi/api/detail/<span class='url-dynamic-parameter-tenant'>{{tenantCode}}</span>/<span class='url-dynamic-parameter-dataset'>{{datasetCode}}</span></code></pre>
					where: 
					<ul>
						<li><span class='url-dynamic-parameter-tenant'>{{tenantCode}}</span> is the Tenant code of the owner of the stream</li>
						<li><span class='url-dynamic-parameter-dataset'>{{datasetCode}}</span> is the  Dataset code </li>
					</ul>
				</p>
				<p>
					<strong>Base Detail URL  with output in JSON format</strong><pre><code>https://api.smartdatanet.it/metadataapi/api/ckan/2/package_list/<span class='url-dynamic-parameter-package-id'>{{packageId}}</span></code></pre> 
					where:
					<ul>
						<li><span class='url-dynamic-parameter-package-id'>{{packageId}}</span> is the packageId retrieved from the list returned with the search (in ckan format)</li>
					</ul>
				</p>
			
				<strong>Parameters</strong> 
				<table>
					<thead><tr><th>Key</th><th>Desc</th><th>Possible values</th></tr></thead>
					<tbody>
						<tr><td>lang</td><td>Lang for the domain and tags of the dataset/stream</td><td>Language managed: italian (lang=it), english(lang=en)</td></tr>
					</tbody>
				</table>
			</div>
			<h2 id="auth">Authentication for private stream/dataset </h2>
			<div class='indent'>
				<p>To view the metadata of private stream/dataset, is necessary authenticate the call with a <strong>token oAuth</strong>. Below the steps to follow</p>
				<ol>
					<li>In the section  <cite><strong>Subscriptions</strong></cite> in the   <a href='https://userportal.smartdatanet.it/userportal' target='_blank'>Userportal</a> (visible only if logged in)
						subscribe  the stream/dataset</li>
					<li>The Userportal will provide than the <strong>token oAuth</strong></li>
					<li>In the HTTP header of the call, insert the attribute  <pre><code>'Authorization'  'Bearer IlMioTokenOauth'</code></pre></li>
				</ol>
				<p>More details on  the <a href='http://developer.smartdatanet.it/docs/utilizzo-dello-store-e-dei-token-oauth-2/' target='_blank'>Developer Center</a> in the section
				<strong>Come accedere ai servizi di lettura autenticandosi con Oauth</strong>
			</div>
		</div>
		<footer>
			<div>
				<a class="logo-sdp" href="http://www.smartdatanet.it" target='_blank'>Smartdatenet.it</a> | 
				<a class="logo-rp" href="http://www.regione.piemonte.it" target='_blank'>Regione Piemonte</a> | 
				<a class="logo-csi" href="http://www.csipiemonte.it" target='_blank'><span class="hidden">CSI Piemonte</span></a> 
			</div>
		</footer>
	</body>
</html>

