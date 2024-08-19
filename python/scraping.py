from selenium import webdriver
from selenium.webdriver.common.by import By
import json
import time
import pandas as pd
from tqdm import tqdm

# Set up Selenium WebDriver dengan pengaturan headless dan argumen tambahan
options = webdriver.ChromeOptions()
options.add_argument('--headless')  # Operasikan dalam mode headless
options.add_argument('--no-sandbox')
options.add_argument('--disable-dev-shm-usage')
options.add_argument('--disable-gpu')
options.add_argument('--window-size=1920x1080')
options.add_argument('--disable-extensions')
options.add_argument('--proxy-server="direct://"')
options.add_argument('--proxy-bypass-list=*')
options.add_argument('--start-maximized')
options.add_argument('--ignore-certificate-errors')
options.add_argument('--allow-running-insecure-content')

driver = webdriver.Chrome(options=options)

# Buka halaman web terlebih dahulu untuk memastikan session yang diperlukan terinisialisasi
initial_url = 'https://www.visitjeju.net/'
driver.get(initial_url)

# Tunggu beberapa detik agar halaman selesai dimuat sepenuhnya
time.sleep(3)

# Daftar kategori dan tag
categories_and_tags = {
    'nature': {'cate1cd': 'cate0000000002', 'tag': 'naturalscenery,'},
    'attraction': {'cate1cd': 'cate0000000002', 'tag': 'attractions,'},
    'art_cultures': {'cate1cd': 'cate0000000002', 'tag': 'culturetourism,'},
    'experiences': {'cate1cd': 'cate0000000002', 'tag': 'experience,'},
    'stay': {'cate1cd': 'cate0000000004', 'tag': 'accommodations,'},
    'food': {'cate1cd': 'cate0000000005', 'tag': 'cuisine,'},
    'shop': {'cate1cd': 'cate0000000003', 'tag': 'shopping,'},
    'festivals': {'cate1cd': 'cate0000001360', 'tag': ''},
}

# URL dasar API
base_api_url = 'https://api.visitjeju.net/api/contents/list?_siteId=jejuavj&locale=en&device=pc&sorting=reviewcnt+desc,+title_en+asc&region1cd=&region2cd=&pageSize=1000&page=1'

# Dictionary untuk menyimpan DataFrames
dataframes = {}

# Loop melalui setiap kategori dan tag dengan progress bar
for category, params in tqdm(categories_and_tags.items(), desc="Scraping Categories"):
    # Bentuk URL API dengan cate1cd dan tag yang sesuai
    api_url = f"{base_api_url}&cate1cd={params['cate1cd']}&tag={params['tag']}"
    
    # Kunjungi URL API
    driver.get(api_url)

    # Tunggu sebentar untuk memastikan halaman dimuat
    time.sleep(3)

    # Ambil halaman sumber (source) yang berisi JSON
    page_source = driver.find_element(By.TAG_NAME, "pre").text

    # Parse JSON dari page_source
    data = json.loads(page_source)

    # Convert items to DataFrame
    df = pd.json_normalize(data['items'])

    # Simpan DataFrame ke dalam dictionary dengan nama kategori sebagai kunci
    dataframes[category] = df

    # Tampilkan sebagian data untuk verifikasi
    print(f"Category: {category}")
    print(df.head())
    print("\n")

# Tutup browser setelah selesai
driver.quit()

# Tambah kolom bernama category ke setiap DataFrame
for category, df in dataframes.items():
    df['category'] = category

# Gabungkan semua DataFrame menjadi satu
combined_df = pd.concat(dataframes.values(), keys=dataframes.keys())

# Pilih hanya kolom yang relevan
# combined_df = combined_df[['alltag', 'title', 'address', 'roadaddress', 'tag', 'introduction', 'latitude', 'longitude', 'postcode', 'phoneno', 'sbst', 'contentscd.label', 'category']]

# Simpan ke file CSV
combined_df.to_csv('visitjeju_data.csv', index=False)
