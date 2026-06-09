package org.example.services;

import com.fasterxml.jackson.core.type.TypeReference;
import org.example.entities.Ticket;
import org.example.entities.Train;
import org.example.entities.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.util.UserServiceUtil;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserBookinService {

    private User user;

    private List<User> userList;

    private ObjectMapper objectMapper = new ObjectMapper();

    private static final String Users_Path = "app/src/main/java/org/example/localdb/user.json";


    public UserBookinService(User user) throws IOException {
        this.user = user;
        loadUsers();


    }
    public UserBookinService() throws IOException{
        loadUsers();

    }
    public void loadUsers() throws  IOException{

        File users = new File(Users_Path);

        userList = objectMapper.readValue(users, new TypeReference<List<User>>() {
        });
    }

    public Boolean loginUser() {
        Optional<User> foundUser = userList.stream().filter(
                user1 -> {
                    return user1.getName().equals(user.getName()) && UserServiceUtil.checkPassword(user.getPassword(), user1.getHashPassword());

                }
        ).findFirst();
        return foundUser.isPresent();
    }

    public Boolean signUp(User user1) {
        try {
            userList.add(user1);
            savedUserListTOFile();
            return Boolean.TRUE;
        } catch (IOException e) {
            return Boolean.FALSE;
        }

    }
    private void savedUserListTOFile() throws  IOException{
        File userFile=new File(Users_Path);
        objectMapper.writeValue(userFile,userList);
    }

    public void fetchBooking(){
        user.printTicket();
    }
    public Boolean cancelBooking(String ticketid) throws  IOException{
        List<Ticket> ticketList=user.getTicketBooked();
        Optional<Ticket> foundTicket=ticketList.stream().filter(
                ticket1 ->{
                    return ticket1.getTicketId().equals(ticketid);
                }
        ).findFirst();
        if(foundTicket.isPresent()){
            ticketList.remove(foundTicket.get());

            objectMapper.writeValue(
                    new File(Users_Path),
                    userList
            );
            return true;

        }
        return false;
    }
    public List<Train> getTrains(String source,String destination){
        try{
            TrainService trainService=new TrainService();
            return trainService.searchTrain(source,destination);
        }catch (IOException e){
            return  new ArrayList<>();
        }
    }

    public  List<List<Integer>> fetchSeats(Train train){
        return train.getSeats();
    }

    public Boolean bookTrainSeat(Train train , int row ,int seat){
        try{
            TrainService trainService=new TrainService();
            List<List<Integer>> seats =train.getSeats();
            if(row>=0 && row< seats.size() && seat >=0 && seat < seats.get(row).size()) {
                if (seats.get(row).get(seat) == 0) {
                    seats.get(row).set(seat, 1);
                    train.setSeats(seats);
                    trainService.addTrain(train);
                    return true;
                } else {
                    return false;
                }
            }else{
                return false;

            }
        } catch (IOException e) {
            return Boolean.FALSE;
        }
    }

}


