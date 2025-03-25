package org.example.Model;

public class Player {
    private int id;
    private String name;
    private int age;
    private String position;
    private String nationality;
    private String photo;

    public Player(int id, String name, String position, int age, String nationality, String photo) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.position = position;
        this.nationality = nationality;
        this.photo = photo;
    }
    public Player() {
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return name + " - " + position + " (" + nationality + "), Yaş: " + age + ", Fotoğraf: " + photo;
    }


    // Kadroyu dosyaya yazmak için string'e dönüştür
    public String toDataString() {
        return id + ";" + name + ";" + position + ";" + age + ";" + nationality + ";" + photo;
    }

    // Dosyadan okuma sırasında string'i Player nesnesine çevir
    public static Player fromDataString(String data) {
        String[] parts = data.split(";");
        return new Player(
                Integer.parseInt(parts[0]),
                parts[1],
                parts[2],
                Integer.parseInt(parts[3]),
                parts[4],
                parts[5]
        );
    }
}
