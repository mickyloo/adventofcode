use adv_code_2024::*;
use anyhow::*;
use code_timing_macros::time_snippet;
use const_format::concatcp;
use itertools::Itertools;
use std::fs::File;
use std::io::{BufRead, BufReader};

const DAY: &str = "22"; // TODO: Fill the day
const INPUT_FILE: &str = concatcp!("input/", DAY, ".txt");

const TEST: &str = "\
1
10
100
2024";

fn main() -> Result<()> {
    start_day(DAY);

    //region Part 1
    println!("=== Part 1 ===");

    fn secret(num: usize) -> usize {
        let step1 = (num ^ (num << 6)) & 0xFFFFFF;
        let step2 = (step1 ^ (step1 >> 5)) & 0xFFFFFF;
        let step3 = (step2 ^ (step2 << 11)) & 0xFFFFFF;
        step3
    }

    fn part1<R: BufRead>(reader: R) -> Result<usize> {
        // TODO: Solve Part 1 of the puzzle
        let numbers = reader
            .lines()
            .map(|l| l.unwrap().parse::<usize>().unwrap())
            .collect_vec();

        let secrets = numbers
            .iter()
            .map(|x| {
                let mut num = x.clone();
                for _ in 0..2000 {
                    num = secret(num);
                }
                num
            })
            .collect_vec();

        let answer = secrets.iter().sum();
        Ok(answer)
    }

    // TODO: Set the expected answer for the test input
    assert_eq!(37327623, part1(BufReader::new(TEST.as_bytes()))?);

    let input_file = BufReader::new(File::open(INPUT_FILE)?);
    let result = time_snippet!(part1(input_file)?);
    println!("Result = {}", result);
    //endregion

    //region Part 2
    // println!("\n=== Part 2 ===");
    //
    // fn part2<R: BufRead>(reader: R) -> Result<usize> {
    //     Ok(0)
    // }
    //
    // assert_eq!(0, part2(BufReader::new(TEST.as_bytes()))?);
    //
    // let input_file = BufReader::new(File::open(INPUT_FILE)?);
    // let result = time_snippet!(part2(input_file)?);
    // println!("Result = {}", result);
    //endregion

    Ok(())
}
