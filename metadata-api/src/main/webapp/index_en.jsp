<html>
	<head>
		<link rel="shortcut icon" href="favicon.png">
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<title>Smart Data Platform - Metadata REST API </title>
		<style>
			body{font-family: arial, sans-serif; letter-spacing: .01em; background-color: #f7f7f7; font-size: 11pt; line-height: 1.5em;margin: 0;}
			header{clear: both; overflow: hidden; margin: 48px  auto 0  auto; width: 80%; }
			header .logo{float: right;}
			header h1{float: left;}
			a{color: #009640;}
			.lang-menu{ position: absolute; top:12px; right: 10%;float: right; font-size: 12px; font-weight: normal; }
			.lang-menu a, .lang-menu strong{margin-left:1em }
			h2{margin-top: 2em; font-size: 32px;}
			.indent{padding-left: 24px;}
			.content{margin: 24px  auto; width: 80%; }
			.row-title td{background-color: #ebebeb; font-weight: bold;}
			.row-title a{padding-left: 2em;}
			.deprecated{color: #bbb; }
			.deprecated pre{border-color: #ccc; background-color: #fafafa;}
			.swagger-deprecated{background: url("./img/swaggerLogoDeprecated.png") center left no-repeat; background-size: 24px 24px; padding-left: 30px; line-height: 24px; display: inline-block; margin-left: 2em;color: #ccc;}

			pre{display: block; padding: 1em; background-color: white; border: solid 1px #009640; }
			table{display: block;  margin-top: 1em;border-collapse: collapse;font-size: 11pt; }
			td, th{ border: solid 1px #ebebeb; padding: .7em;}
			td{background-color: white; }
			th{background-color: #ebebeb; text-align: left; }
			
			.main-menu a{display: inline-block;font-size: 18px;  text-decoration: none; }
			.main-menu a.main-menu-item{ padding: 1em; }
			.main-menu a:hover{ text-decoration: none; background-color: #009640; color: white;}
			.main-menu{border: solid black; border-width: 1px 0; padding: 0;}
			.main-menu a.swagger-menu-item{background: url("./img/swaggerLogoColor.png") 18px center  no-repeat; background-size: 32px 32px; padding: 1em 1em 1em 60px; }
			.main-menu a.swagger-menu-item:hover{background: #009640 url("./img/swaggerLogoWhite.png") 18px center no-repeat;}

			.url-dynamic-parameter-tenant{color:  #60a917; font-weight: bold;}
			.url-dynamic-parameter-smartobject{color:  #0050ef;font-weight: bold;}
			.url-dynamic-parameter-stream{color:  #d80073;font-weight: bold;}
			.url-dynamic-parameter-dataset{color:  #ba00ff;font-weight: bold;}
			.url-dynamic-parameter-package-id{color:  #ff9900;font-weight: bold;}
			ol li {padding: .7em;}
			
			.deprecated .url-dynamic-parameter-tenant,
			.deprecated .url-dynamic-parameter-smartobject,
			.deprecated .url-dynamic-parameter-stream,
			.deprecated .url-dynamic-parameter-dataset,
			.deprecated .url-dynamic-parameter-package-id{color:  #bbb;font-weight: bold;}
			
			footer{background-color: black; color: white; padding: 24px; text-align: center;}
			footer  a{color: white; text-decoration: none; padding: 0 48px;}

		</style>
	</head>
	<body>
		<header>
			<h1>Smartdatanet Metadata API</h1>
			<div class='logo'>
				<img src='https://userportal.smartdatanet.it/userportal/img/familyidentity/logosdp-top-navbar.png' alt='Smartdatanet'/>
			</div>
			<div class='lang-menu'>
					<a href='index.jsp'>Italiano</a>
					<strong>English</strong>
			</div>

		</header>
		<div class='content'>
			<p>Search and Detail API  REST for streams and datasets metadata hosted on  the SmartData Platform<p>
			<p class='main-menu'>
				<a href="#search" class='main-menu-item'>Search</a>
				<a href="#detail"span class='main-menu-item'>Detail</a>
				<a href="#auth" class='main-menu-item'>Authentication for private stream/dataset</a>
				<a href="/metadataapi/docs/api/v02/" class='swagger-menu-item'>Swagger Doc</a>
			</p>
			<h2 id="search">Search</h2>
			<div class='indent'>
				<div>
					<strong>Base search Url format JSON</strong><pre><code>https://api.smartdatanet.it/metadataapi/api/v02/search?</code></pre>
				</div>
				<div>
					<strong>Base search Url format CKAN</strong><pre><code>https://api.smartdatanet.it/metadataapi/api/ckan/2/package_list?</code></pre>
				</div>
				<div>
					<strong>Base Url Data Catalog Interoperability Protocol (DCAT-AP-IT) format JSON+LD</strong><pre><code>https://api.smartdatanet.it/metadataapi/api/dcat/dataset_list?</code></pre>
				</div>
				<div class='deprecated'>
					<i>V01 (deprecated) Base search Url format JSON</i> <a href="/metadataapi/docs/api/v01/" class='swagger-deprecated'>Swagger Doc</a>
					<pre><code>https://api.smartdatanet.it/metadataapi/api/search/full?</code></pre>
				</div>
				<strong>Parameters</strong> 
				<table>
					<thead><tr><th>Key</th><th>Desc</th><th>Possible values</th></tr></thead>
					<tbody>
						<tr><td>q</td><td>search query</td><td>Free text</td></tr>
						<tr><td>lang</td><td>Lang for the domain and tags of the dataset/stream</td><td>Language managed: italian (lang=it), english(lang=en)</td></tr>
						<tr class='row-title'><td colspan='3'>Filter</td></tr>
						<tr><td>tenant</td><td>Tenant code of the owner of the stream/dataset</td><td>If used a unkonwn tenant code returns zero values</td></tr>
						<tr><td>organization</td><td>Organization code of the owner of the stream/dataset</td><td>If used a unkonwn tenant code returns zero values</td></tr>
						<tr><td>domain</td><td>Domain code of the stream/dataset (Agriculture, Energy &hellip;)</td><td>If used a unkonwn domain code returns zero values </td></tr>
						<tr><td>opendata</td><td>Properties of the dataset/stream  to be opendata</td><td>If used opendata=true will be extract only opendata, if used any other value will be extract only NOT opendata. If omitted extract opendata and not opendata</td></tr>
						<tr><td>tags</td><td>List of tags of dataset/stream</td><td>List of tagsCode comma separated</td></tr>
						<tr><td>visibility</td><td>Filter on visibility</td><td>public/private</td></tr>
						<tr><td>isSearchExact</td><td>If true does exact match</td><td>Boolean (true/false). Default false.</td></tr>
						<tr><td>includeSandbox</td><td>If true includes data from sandbox</td><td>Boolean (true/false). Default false.</td></tr>
						<tr><td>hasStream</td><td>If true includes only dataset/stream with a stream (CKAN and DCAT doesn't accept this parameter) </td><td>Boolean (true/false).</td></tr>
						<tr><td>hasDataset</td><td>If true includes only dataset/stream with a dataset (CKAN and DCAT doesn't accept this parameter) </td><td>Boolean (true/false).</td></tr>
						<tr><td>externalReference</td><td>Seaqrch for externalReference (wildcards permitted)</td><td>String field</td></tr>
						<tr class='row-title'><td colspan='3'>Pagination</td></tr>
						<tr><td>start</td><td>Pagination: first  row that will be extract</td><td>Numeric (if omitted, it will used zero)</td></tr>
						<tr><td>end</td><td>Pagination: number of row that will be extract</td><td>Numeric (if omitted, it will used 12). Max value: 1000</td></tr>
						<tr class='row-title'><td colspan='3'>Geolocalized search</td></tr>
						<tr><td>minLat</td><td>Minimal latitude of square</td><td>Numeric Field min=-90.0 max=90.0 </td></tr>
						<tr><td>minLon</td><td>Minimal longitude of square</td><td>Numeric Field min=-180.0 max=180.0</td></tr>
						<tr><td>maxLat</td><td>Max latitude of square</td><td>Numeric Field min=-90.0 max=90.0</td></tr>
						<tr><td>maxLon</td><td>Max longitude of square</td><td>Numeric Field min=-180.0 max=180.0</td></tr>
						<tr class='row-title'><td colspan='3'>Facet <a href=' https://cwiki.apache.org/confluence/display/solr/Faceting' target='_blank'>Documentazione su Apache Solr</a></td></tr>
						<tr><td>facet.field</td><td>List of comma separate fields to be treated as a facet</td><td>Eg. tenatCode,domainCode</td></tr>
						<tr><td>facet.prefix</td><td>Limits the terms used for faceting to those that begin with the specified prefix</td></td></tr>
						<tr><td>facet.sort</td><td>Controls how faceted results are sorted</td></tr>
						<tr><td>facet.contains</td><td>Limits the terms used for faceting to those that contain the specified substring</td></tr>
						<tr><td>facet.contains.ignoreCase</td><td>If facet.contains is used, ignore case when searching for the specified substring</td></tr>
						<tr><td>facet.limit</td><td>Controls how many constraints should be returned for each facet</td></tr>
						<tr><td>facet.offset</td><td>Specifies an offset into the facet results at which to begin displaying facets</td></tr>
						<tr><td>facet.mincount</td><td>Specifies the minimum counts required for a facet field to be included in the response</td></tr>
						<tr><td>facet.missing</td><td>Controls whether Solr should compute a count of all matching results which have no value for the field, in addition to the term-based constraints of a facet</td></tr> 
					</tbody>
				</table>
			</div>
			<h2 id="detail">Detail</h2>
			<div class='indent'>
				<div>
					<strong>Base Detail URL for stream with output in JSON format</strong><pre><code>https://api.smartdatanet.it/metadataapi/api/v02/detail/<span class='url-dynamic-parameter-tenant'>{{tenantCode}}</span>/<span class='url-dynamic-parameter-smartobject'>{{smartobjectCode}}</span>/<span class='url-dynamic-parameter-stream'>{{streamCode}}</span>?</code></pre>
				where: 
					<ul>
						<li><span class='url-dynamic-parameter-tenant'>{{tenantCode}}</span> is the Tenant code of the owner of the stream</li>
						<li><span class='url-dynamic-parameter-smartobject'>{{smartobjectCode}}</span> is the  Smart Object code used by  the stream</li>
						<li><span class='url-dynamic-parameter-stream'>{{streamCode}}</span>is the  Stream code</li>
					</ul>
				</div>
				<div>
					<strong>Base Detail URL for dataset with output in JSON format</strong><pre><code>https://api.smartdatanet.it/metadataapi/api/v02/detail/<span class='url-dynamic-parameter-tenant'>{{tenantCode}}</span>/<span class='url-dynamic-parameter-dataset'>{{datasetCode}}</span></code></pre>
					where: 
					<ul>
						<li><span class='url-dynamic-parameter-tenant'>{{tenantCode}}</span> is the Tenant code of the owner of the stream</li>
						<li><span class='url-dynamic-parameter-dataset'>{{datasetCode}}</span> is the  Dataset code </li>
					</ul>
				</div>
				<div>
					<strong>Base Detail URL  with output in CKAN format</strong><pre><code>https://api.smartdatanet.it/metadataapi/api/ckan/2/package_list/<span class='url-dynamic-parameter-package-id'>{{packageId}}</span></code></pre> 
					where:
					<ul>
						<li><span class='url-dynamic-parameter-package-id'>{{packageId}}</span> is the packageId retrieved from the list returned with the search (in ckan format)</li>
					</ul>
				</div>
				<div  class='deprecated'>
					<i>V01 (deprecated) Base Detail URL for stream with output in JSON format</i> <a href="/metadataapi/docs/api/v01/" class='swagger-deprecated'>Swagger Doc</a>
					<pre><code>https://api.smartdatanet.it/metadataapi/api/detail/<span class='url-dynamic-parameter-tenant'>{{tenantCode}}</span>/<span class='url-dynamic-parameter-smartobject'>{{smartobjectCode}}</span>/<span class='url-dynamic-parameter-stream'>{{streamCode}}</span>?</code></pre>
				</div>
				<div class='deprecated'>
					<i>V01 (deprecated) Base Detail URL for dataset with output in JSON format</i> <a href="/metadataapi/docs/api/v01/" class='swagger-deprecated'>Swagger Doc</a>
					<pre><code>https://api.smartdatanet.it/metadataapi/api/v02/detail/<span class='url-dynamic-parameter-tenant'>{{tenantCode}}</span>/<span class='url-dynamic-parameter-dataset'>{{datasetCode}}</span></code></pre>
				</div>
			
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
				<div>To view the metadata of private stream/dataset, is necessary authenticate the call with a <strong>token oAuth</strong>, adding in the headet HTTP the attribute 
				<pre><code>'Authorization'  'Bearer myTokenOauth'</code></pre>
				Is possible to use any token recovered from Userportal to find private stream / dataset visible from the user owner of the token.
				<p>The oAuth token are avaible in the section  <cite><strong>Subscriptions</strong></cite> in the   <a href='https://userportal.smartdatanet.it/userportal' target='_blank'>Userportal</a> (visible only if logged in)</p>
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

